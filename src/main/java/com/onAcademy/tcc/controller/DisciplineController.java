package com.onAcademy.tcc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.repository.ClassStRepo;
import com.onAcademy.tcc.repository.DisciplineRepo;
import com.onAcademy.tcc.service.DisciplineService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Discipline", description = "EndPoint de disciplina")
@RestController
@RequestMapping("/api")
public class DisciplineController {

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private ClassStRepo classStRepo;

    @Autowired
    private DisciplineRepo disciplineRepository;

    record DisciplineDTO(String nomeDisciplina) {}

    @PostMapping("/discipline")
    public ResponseEntity<String> criarDiscipline(@RequestBody Discipline discipline) {
        try {
            Discipline disciplineUnica = disciplineService.criarDiscipline(discipline);
            return ResponseEntity.status(HttpStatus.CREATED).body("Disciplina criada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar disciplina.");
        }
    }

    @GetMapping("/discipline")
    public ResponseEntity<List<Discipline>> buscarDisciplines() {
        List<Discipline> buscarDisciplines = disciplineService.buscarDisciplines();
        return ResponseEntity.ok(buscarDisciplines);
    }

    @PutMapping("/discipline/{id}")
    public ResponseEntity<String> editarDiscipline(@PathVariable Long id, @RequestBody Discipline discipline) {
        try {
            Discipline editarDiscipline = disciplineService.atualizarDiscipline(id, discipline);
            if (editarDiscipline != null) {
                return ResponseEntity.ok("Disciplina atualizada com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Disciplina não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar disciplina.");
        }
    }

    @GetMapping("/discipline/{id}")
    public ResponseEntity<?> buscarUnica(@PathVariable Long id) {
        try {
            Discipline buscarUnica = disciplineService.buscarUnicaDisciplina(id);
            if (buscarUnica != null) {
                return ResponseEntity.ok(buscarUnica);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Disciplina não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar disciplina.");
        }
    }

    @DeleteMapping("/discipline/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        try {
            Discipline deletado = disciplineService.deleteDiscipline(id);
            if (deletado != null) {
                return ResponseEntity.ok("Disciplina deletada com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Disciplina não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
