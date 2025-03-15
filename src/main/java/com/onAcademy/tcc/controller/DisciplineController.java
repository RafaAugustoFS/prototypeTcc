package com.onAcademy.tcc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * Record DTO para representar uma disciplina de forma simplificada.
     */
    record DisciplineDTO(Long id, String nomeDisciplina) {
    }

    /**
     * Cria uma nova disciplina.
     *
     * @param discipline Objeto contendo os dados da disciplina.
     * @return ResponseEntity com a disciplina criada ou uma mensagem de erro.
     */
    @PostMapping("/discipline")
    public ResponseEntity<?> criarDiscipline(@Valid @RequestBody Discipline discipline) {
        try {
            if (discipline.getNomeDisciplina() == null || discipline.getNomeDisciplina().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome da disciplina é obrigatório.");
            }
            Discipline disciplinaCriada = disciplineService.criarDiscipline(discipline);
            return new ResponseEntity<>(disciplinaCriada, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar disciplina.");
        }
    }

    /**
     * Retorna uma lista de todas as disciplinas.
     *
     * @return ResponseEntity contendo a lista de disciplinas no formato DTO.
     */
    @GetMapping("/discipline")
    public ResponseEntity<List<DisciplineDTO>> buscarDisciplinas() {
        List<DisciplineDTO> disciplinas = disciplineService.buscarDisciplines().stream()
            .map(discipline -> new DisciplineDTO(discipline.getId(), discipline.getNomeDisciplina()))
            .toList();
        return ResponseEntity.ok(disciplinas);
    }

    /**
     * Atualiza os dados de uma disciplina existente.
     *
     * @param id ID da disciplina a ser atualizada.
     * @param discipline Objeto contendo os novos dados da disciplina.
     * @return ResponseEntity com a disciplina atualizada ou uma mensagem de erro.
     */
    @PutMapping("/discipline/{id}")
    public ResponseEntity<?> editarDiscipline(@PathVariable Long id, @Valid @RequestBody Discipline discipline) {
        try {
            Discipline disciplinaAtualizada = disciplineService.atualizarDiscipline(id, discipline);
            if (disciplinaAtualizada != null) {
                return new ResponseEntity<>(disciplinaAtualizada, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Disciplina não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar disciplina.");
        }
    }

    /**
     * Busca uma disciplina pelo ID.
     *
     * @param id ID da disciplina a ser buscada.
     * @return ResponseEntity com a disciplina encontrada ou uma mensagem de erro.
     */
    @GetMapping("/discipline/{id}")
    public ResponseEntity<?> buscarDisciplinaPorId(@PathVariable Long id) {
        try {
            Discipline disciplina = disciplineService.buscarUnicaDisciplina(id);
            if (disciplina != null) {
                return ResponseEntity.ok(disciplina);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Disciplina não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar disciplina.");
        }
    }

    /**
     * Exclui uma disciplina pelo ID.
     *
     * @param id ID da disciplina a ser excluída.
     * @return ResponseEntity com uma mensagem de sucesso ou erro.
     */
    @DeleteMapping("/discipline/{id}")
    public ResponseEntity<String> deletarDisciplina(@PathVariable Long id) {
        try {
            Discipline disciplinaDeletada = disciplineService.deleteDiscipline(id);
            if (disciplinaDeletada != null) {
                return ResponseEntity.ok("Disciplina deletada com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Disciplina não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}