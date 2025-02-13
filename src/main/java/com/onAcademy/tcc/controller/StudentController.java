package com.onAcademy.tcc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.onAcademy.tcc.dto.LoginStudent;
import com.onAcademy.tcc.dto.StudentClassDTO;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.service.StudentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Student", description = "EndPoint de estudante")

@RestController
@RequestMapping("/api")
public class StudentController {
	@Autowired
	private StudentService studentService;

	record ClassDTO(String nomeTurma, Long idTUrma) {
	};

	record NoteDTO(Long idNota, Double nota, Discipline discipline) {};
	
//	record DisciplineDTO(String nomeDisciplina, Long idDiscipline)
	
	record StudentDTO(String nome, String dataNascimentoAluno,String telefoneAluno, String emailAluno, String matriculaAluno, ClassDTO turma, List<NoteDTO> notas) {}
	


	@PostMapping("/student/login")
	public ResponseEntity<Map<String, String>> loginStudent(@RequestBody LoginStudent loginStudent) {
		String token = studentService.loginStudent(loginStudent.matriculaAluno(), loginStudent.senhaAluno());
		 Map<String,String> response = new HashMap<>();
	        response.put("token", token);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	@PreAuthorize("hasRole('INSTITUTION')")
	@PostMapping("/student")
	public ResponseEntity<Student> criarEstudante(@RequestBody StudentClassDTO studentDTO) {
		Student student1 = studentService.criarEstudante(studentDTO);
		return new ResponseEntity<>(student1, HttpStatus.CREATED);
	}

	@GetMapping("/student")
	public ResponseEntity<List<Student>> buscarTodosEstudantes() {
		List<Student> student = studentService.buscarTodosEstudantes();

		return new ResponseEntity<>(student, HttpStatus.OK);
	}

	@GetMapping("/student/{id}")
	public ResponseEntity<StudentDTO> buscarEstudanteUnico(@PathVariable Long id) {
		Student buscaEstudante = studentService.buscarEstudanteUnico(id);
		if(buscaEstudante != null) {
			 List<NoteDTO> notas = buscaEstudante.getNotas().stream()
                     .map(nota -> new NoteDTO(nota.getId(),nota.getNota(), nota.getDisciplineId()))
                     .collect(Collectors.toList());
			 
         	var turma = new ClassDTO(buscaEstudante.getClassSt().getNomeTurma(), buscaEstudante.getClassSt().getId());
			var studentDTO = new StudentDTO(buscaEstudante.getNomeAluno(), 
					buscaEstudante.getDataNascimentoAluno().toString(), 
					buscaEstudante.getTelefoneAluno(), 
					buscaEstudante.getEmailAluno(), 
					buscaEstudante.getIdentifierCode(),
					turma, notas);
			return new ResponseEntity<>(studentDTO, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/student/{id}")
	public ResponseEntity<Student> atualizarEstudante(@PathVariable Long id, @RequestBody Student student) {
		Student atualizarEstudante = studentService.atualizarEstudante(id, student);
		if (atualizarEstudante != null) {
			return new ResponseEntity<>(atualizarEstudante, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/student/{id}")
	public ResponseEntity<Student> deletarEstudante(@PathVariable Long id) {
		Student deletarEstudante = studentService.deletarEstudante(id);
		if (deletarEstudante != null) {
			return new ResponseEntity<>(deletarEstudante, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
