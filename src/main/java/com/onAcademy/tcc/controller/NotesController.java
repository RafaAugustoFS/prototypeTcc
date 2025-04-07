package com.onAcademy.tcc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.onAcademy.tcc.dto.NoteDTO;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.model.Note;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.repository.DisciplineRepo;
import com.onAcademy.tcc.repository.StudentRepo;
import com.onAcademy.tcc.service.NoteService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notes", description = "EndPoint de boletim")
@RestController
@RequestMapping("/api")
public class NotesController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private DisciplineRepo disciplineRepo;

    /**
     * Cria uma nova nota para um aluno.
     *
     * @param noteDTO DTO contendo os dados da nota.
     * @return ResponseEntity com a nota criada ou uma mensagem de erro.
     */
    @PostMapping("/note")
    public ResponseEntity<?> criarNota(@RequestBody NoteDTO noteDTO) {
        try {
            validarNoteDTO(noteDTO);

            Student student = studentRepo.findById(noteDTO.getStudentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado."));

            Discipline discipline = disciplineRepo.findById(noteDTO.getDisciplineId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada."));

            Note note = new Note();
            note.setStudentId(student);
            note.setNota(noteDTO.getNota());
            note.setBimestre(noteDTO.getBimestre());
            note.setDisciplineId(discipline);
            note.setStatus(noteDTO.getNota() > 5 ? "Aprovado" : "Reprovado");

            Note notaCriada = noteService.criarNotas(note);
            return ResponseEntity.status(HttpStatus.CREATED).body(notaCriada);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar nota: " + e.getMessage());
        }
    }

    /**
     * Valida os dados da nota.
     *
     * @param noteDTO DTO contendo os dados da nota.
     * @throws IllegalArgumentException Se algum campo obrigatório estiver inválido.
     */
    private void validarNoteDTO(NoteDTO noteDTO) {
        if (noteDTO.getStudentId() == null) {
            throw new IllegalArgumentException("Por favor, preencha o campo student.");
        }
        if (noteDTO.getNota() == null || noteDTO.getNota() < 0 || noteDTO.getNota() > 10) {
            throw new IllegalArgumentException("Nota inválida. Deve estar entre 0 e 10.");
        }
        if (noteDTO.getDisciplineId() == null) {
            throw new IllegalArgumentException("Por favor, preencha o campo disciplina.");
        }
        if (noteDTO.getBimestre() < 1 || noteDTO.getBimestre() > 4) {
            throw new IllegalArgumentException("Bimestre inválido. Deve estar entre 1 e 4.");
        }
    }

    /**
     * Retorna uma lista de todas as notas.
     *
     * @return ResponseEntity com a lista de notas.
     */
    @GetMapping("/note")
    public ResponseEntity<List<Note>> buscarTodasNotas() {
        List<Note> notas = noteService.buscarNotas();
        return ResponseEntity.ok(notas);
    }

    /**
     * Busca uma nota pelo ID.
     *
     * @param id ID da nota a ser buscada.
     * @return ResponseEntity com a nota encontrada ou uma mensagem de erro.
     */
    @GetMapping("/note/{id}")
    public ResponseEntity<?> buscarNotaPorId(@PathVariable Long id) {
        try {
            Note nota = noteService.buscarNotaUnica(id);
            if (nota == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nota não encontrada.");
            }
            return ResponseEntity.ok(nota);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar nota: " + e.getMessage());
        }
    }

    /**
     * Atualiza uma nota existente.
     *
     * @param id ID da nota a ser atualizada.
     * @param note Objeto contendo os novos dados da nota.
     * @return ResponseEntity com a nota atualizada ou uma mensagem de erro.
     */
    @PutMapping("/note/{id}")
    public ResponseEntity<?> atualizarNota(@PathVariable Long id, @RequestBody NoteDTO noteDTO) {
        try {
            validarNoteDTO(noteDTO);
            
            Note existingNote = noteService.buscarNotaUnica(id);
            if (existingNote == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nota não encontrada.");
            }

            // Atualizar entidades relacionadas se necessário
            if (!existingNote.getStudentId().getId().equals(noteDTO.getStudentId())) {
                Student student = studentRepo.findById(noteDTO.getStudentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado."));
                existingNote.setStudentId(student);
            }

            if (!existingNote.getDisciplineId().getId().equals(noteDTO.getDisciplineId())) {
                Discipline discipline = disciplineRepo.findById(noteDTO.getDisciplineId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada."));
                existingNote.setDisciplineId(discipline);
            }

            // Atualizar campos simples
            existingNote.setNota(noteDTO.getNota());
            existingNote.setBimestre(noteDTO.getBimestre());
            existingNote.setStatus(noteDTO.getNota() > 5 ? "Aprovado" : "Reprovado");

            Note notaAtualizada = noteService.atualizarNotas(id, existingNote);
            return ResponseEntity.ok(notaAtualizada);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao atualizar nota: " + e.getMessage());
        }
    }
   
    /**
     * Exclui uma nota pelo ID.
     *
     * @param id ID da nota a ser excluída.
     * @return ResponseEntity com a nota excluída ou uma mensagem de erro.
     */
    @DeleteMapping("/note/{id}")
    public ResponseEntity<?> deletarNota(@PathVariable Long id) {
        try {
            Note notaDeletada = noteService.deletarNota(id);
            if (notaDeletada == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nota não encontrada.");
            }
            return ResponseEntity.ok(notaDeletada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar nota: " + e.getMessage());
        }
    }
}