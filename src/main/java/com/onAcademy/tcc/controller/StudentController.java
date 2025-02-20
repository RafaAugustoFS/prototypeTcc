package com.onAcademy.tcc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.onAcademy.tcc.dto.LoginStudent;
import com.onAcademy.tcc.dto.StudentClassDTO;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.service.StudentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Student", description = "EndPoint de estudante")
@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    record ClassDTO(String nomeTurma, Long idTurma) {};
    record NoteDTO(Long idNota, Double nota, int bimestre, String status, String nomeDisciplina) {};
    record StudentDTO(String nome, String dataNascimentoAluno, String telefoneAluno, String emailAluno,
                      String matriculaAluno, ClassDTO turma, List<NoteDTO> notas) {};

    @PostMapping("/student/login")
    public ResponseEntity<Map<String, String>> loginStudent(@RequestBody LoginStudent loginStudent) {
        try {
            String token = studentService.loginStudent(loginStudent.identifierCode(), loginStudent.password());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Falha no login: " + e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasRole('INSTITUTION')")
    @PostMapping("/student")
    public ResponseEntity<?> criarEstudante(@RequestBody StudentClassDTO studentDTO) {
        try {
            Student student1 = studentService.criarEstudante(studentDTO);
            return new ResponseEntity<>(student1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao criar estudante: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/student")
    public ResponseEntity<?> buscarTodosEstudantes() {
        try {
            List<Student> students = studentService.buscarTodosEstudantes();
            return new ResponseEntity<>(students, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao buscar estudantes: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> buscarEstudanteUnico(@PathVariable Long id) {
        try {
            Student buscaEstudante = studentService.buscarEstudanteUnico(id);
            if (buscaEstudante != null) {
                List<NoteDTO> notas = buscaEstudante.getNotas().stream().map(nota ->
                        new NoteDTO(nota.getId(), nota.getNota(), nota.getBimestre(), nota.getStatus(),
                                nota.getDisciplineId().getNomeDisciplina())).collect(Collectors.toList());

                var turma = new ClassDTO(buscaEstudante.getClassSt().getNomeTurma(), buscaEstudante.getClassSt().getId());
                var studentDTO = new StudentDTO(buscaEstudante.getNomeAluno(),
                        buscaEstudante.getDataNascimentoAluno().toString(), buscaEstudante.getTelefoneAluno(),
                        buscaEstudante.getEmailAluno(), buscaEstudante.getIdentifierCode(), turma, notas);

                return new ResponseEntity<>(studentDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Map.of("error", "Estudante não encontrado"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao buscar estudante: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/student/{id}")
    public ResponseEntity<?> atualizarEstudante(@PathVariable Long id, @RequestBody Student student) {
        try {
            Student atualizarEstudante = studentService.atualizarEstudante(id, student);
            if (atualizarEstudante != null) {
                return new ResponseEntity<>(atualizarEstudante, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Map.of("error", "Estudante não encontrado"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao atualizar estudante: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/student/{id}")
    public ResponseEntity<?> deletarEstudante(@PathVariable Long id) {
        try {
            Student deletarEstudante = studentService.deletarEstudante(id);
            if (deletarEstudante != null) {
                return new ResponseEntity<>(deletarEstudante, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Map.of("error", "Estudante não encontrado"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao deletar estudante: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
