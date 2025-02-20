package com.onAcademy.tcc.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.service.DisciplineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Discipline", description = "EndPoint de disciplina")
@RestController
@RequestMapping("/api")
public class DisciplineController {

	@Autowired
	private DisciplineService disciplineService;

	record DisciplineDTO(String nomeDisciplina) {
	}


	@PostMapping("/discipline")
	public ResponseEntity<?> criarDiscipline(@RequestBody Discipline discipline) {
		try {
			if (discipline.getNomeDisciplina() == null || discipline.getNomeDisciplina().trim().isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome da disciplina é obrigatório.");
			}
			Discipline disciplineUnica = disciplineService.criarDiscipline(discipline);
			return new ResponseEntity<>(disciplineUnica, HttpStatus.CREATED);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
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
	public ResponseEntity<?> editarDiscipline(@PathVariable Long id, @RequestBody Discipline discipline) {
		try {
			Discipline editarDiscipline = disciplineService.atualizarDiscipline(id, discipline);
			if (editarDiscipline != null) {
				return new ResponseEntity<>(editarDiscipline, HttpStatus.OK);
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
