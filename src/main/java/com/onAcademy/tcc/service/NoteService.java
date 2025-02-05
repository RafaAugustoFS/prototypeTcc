package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.onAcademy.tcc.model.Note;
import com.onAcademy.tcc.repository.NoteRepo;

@Service
public class NoteService {
	private NoteRepo noteRepo;

	public Note criarNotas(Note note) {
		Note criarNota = noteRepo.save(note);
		return criarNota;
	}

	// Listar Eventos
	public List<Note> buscarNotas() {
		List<Note> buscarNotas = noteRepo.findAll();
		return buscarNotas;
	}

	public Note buscarNotaUnica(Long id) {
		Optional<Note> existNote = noteRepo.findById(id);
		if (existNote.isPresent()) {
			return existNote.get();
		}
		return null;
	}

	public Note atualizarNotas(Long id, Note note) {
		Optional<Note> existNote = noteRepo.findById(id);
		if (existNote.isPresent()) {
			Note atualizarNota = existNote.get();
			atualizarNota.setDisciplineId(note.getDisciplineId());
			atualizarNota.setNota(note.getNota());
			atualizarNota.setStatus(note.getStatus());
			atualizarNota.setStudentId(note.getStudentId());
		}

		return null;
	}

	// Deletar Evento
	public Note deletarNota(Long id) {
		Optional<Note> existNote = noteRepo.findById(id);
		if (existNote.isPresent()) {
			Note deletarNote = existNote.get();
			noteRepo.delete(deletarNote);
			return deletarNote;
		}

		return null;
	}

}
