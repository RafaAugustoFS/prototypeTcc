package com.onAcademy.tcc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

	@PostMapping("/note")
	public ResponseEntity<?> criarNotas(@RequestBody NoteDTO noteDTO) {
		try {
			if(noteDTO.getStudentId() == null) {
				throw new IllegalArgumentException("Por favor preencha o campo student.");
			}
			Student student = studentRepo.findById(noteDTO.getStudentId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));

			
			if(noteDTO.getDisciplineId() == null) {
				throw new IllegalArgumentException("Por favor preencha o campo disciplina.");
			}
			Discipline discipline = disciplineRepo.findById(noteDTO.getDisciplineId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada"));

			if(noteDTO.getNota() == null) {
				throw new IllegalArgumentException("Por favor preencha o campo nota.");
			}
			
			Note note = new Note();
			note.setStudentId(student);
			note.setNota(noteDTO.getNota());
			note.setStatus(noteDTO.getStatus());

			
			if (noteDTO.getNota() < 0 || noteDTO.getNota() > 10) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nota deve estar entre 0 e 10");
			}

			
			note.setStatus(note.getNota() > 5 ? "Aprovado" : "Reprovado");

		
			if (noteDTO.getBimestre() < 1 || noteDTO.getBimestre() > 4) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bimestre inválido. Deve estar entre 1 e 4.");
			}

			note.setBimestre(noteDTO.getBimestre());
			note.setDisciplineId(discipline);

		
			noteService.criarNotas(note);

		
			 return ResponseEntity.status(HttpStatus.CREATED).body(note);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/note")
	public ResponseEntity<List<Note>> buscarTodasAsNotas() {
		List<Note> notes = noteService.buscarNotas();
		return new ResponseEntity<>(notes, HttpStatus.OK);
	}

	@GetMapping("/note/{id}")
	public ResponseEntity<?> BuscarNotaUnica(@PathVariable Long id) {

		try {
			Note notaUnica = noteService.buscarNotaUnica(id);
			if (notaUnica != null) {
				return new ResponseEntity<>(notaUnica, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nota não encontrada");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	@PutMapping("/note/{id}")
	public ResponseEntity<?> atualizarNota(@PathVariable Long id, @RequestBody Note note) {
		try {
			Note notaUnica = noteService.buscarNotaUnica(id);
			if (notaUnica != null) {
				return new ResponseEntity<>(notaUnica, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nota não encontrada");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}

	@DeleteMapping("/note/{id}")
	public ResponseEntity<?> deletarNota(@PathVariable Long id) {
		try {
			Note deletarNote = noteService.deletarNota(id);
			if (deletarNote == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nota não encontrada.");
			}
			return ResponseEntity.ok(deletarNote);
		} catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
	}
		

}
