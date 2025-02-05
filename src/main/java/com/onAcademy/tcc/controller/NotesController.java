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
import com.onAcademy.tcc.model.Note;
import com.onAcademy.tcc.service.NoteService;

@RestController
@RequestMapping("/api")
public class NotesController {
	@Autowired
	private NoteService noteService;
	
	@PostMapping("/note")
	public ResponseEntity<Note> criarNotas(@RequestBody Note note) {
		Note nota1 = noteService.criarNotas(note);
		return new ResponseEntity<>(nota1, HttpStatus.CREATED);
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
