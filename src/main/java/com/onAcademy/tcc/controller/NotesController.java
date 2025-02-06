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

import com.onAcademy.tcc.dto.NoteDTO;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.model.Note;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.repository.DisciplineRepo;
import com.onAcademy.tcc.repository.StudentRepo;
import com.onAcademy.tcc.service.NoteService;

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
	public ResponseEntity<Note> criarNotas(@RequestBody NoteDTO noteDTO) {
		Student student = studentRepo.findById(noteDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Discipline discipline = disciplineRepo.findById(noteDTO.getDisciplineId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        // Criar a nova nota e associar o aluno e a disciplina
        Note note = new Note();
        note.setStudentId(student);   // Associa o aluno
        note.setNota(noteDTO.getNota());   // Define a nota
        note.setStatus(noteDTO.getStatus());   // Define o status
        note.setDisciplineId(discipline);   // Associa a disciplina

        // Salvar a nota no banco de dados
        Note notaCriada = noteService.criarNotas(note);
		return new ResponseEntity<>(notaCriada, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/note")
	public ResponseEntity<List<Note>> buscarTodasAsNotas() {
		List<Note> notes = noteService.buscarNotas();
		return new ResponseEntity<>(notes, HttpStatus.OK);
	}

	@GetMapping("/note/{id}")
	public ResponseEntity<Note> BuscarNotaUnica(@PathVariable Long id) {

		Note notaUnica = noteService.buscarNotaUnica(id);
		if (notaUnica != null) {
			return new ResponseEntity<>(notaUnica, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	
	@PutMapping("/note/{id}")
	public ResponseEntity<Note> atualizarNota(@PathVariable Long id, @RequestBody Note note) {
		Note atualizarNota = noteService.atualizarNotas(id, note);
		if (atualizarNota != null) {
			return new ResponseEntity<>(atualizarNota, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	
	@DeleteMapping("/note/{id}")
	public ResponseEntity<Note> deletarNota(@PathVariable Long id) {
		Note deletarNote = noteService.deletarNota(id);
		if (deletarNote != null) {
			return new ResponseEntity<>(deletarNote, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
