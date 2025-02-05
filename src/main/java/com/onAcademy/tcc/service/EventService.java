package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.Event;
import com.onAcademy.tcc.repository.EventRepo;

@Service
public class EventService {
	@Autowired
	private EventRepo eventRepo;

	// Criar Evento
	public Event criarEvento(Event event) {
		Event criarEvento = eventRepo.save(event);
		return criarEvento;
	}

	// Listar Eventos
	public List<Event> buscarEventos() {
		List<Event> buscarEventos = eventRepo.findAll();
		return buscarEventos;
	}

	// Listar Evento Único
	public Event buscarEventoUnico(Long id) {
		Optional<Event> existEvent = eventRepo.findById(id);
		if (existEvent.isPresent()) {
			return existEvent.get();
		}
		return null;
	}

	// Atualizar Envento
	public Event atualizarEvento(Long id, Event event) {
		Optional<Event> existEvent = eventRepo.findById(id);
		if (existEvent.isPresent()) {
			Event atualizarEvento = existEvent.get();
			atualizarEvento.setTituloEvento(event.getTituloEvento());
			atualizarEvento.setLocalEvento(event.getLocalEvento());
			atualizarEvento.setHorarioEvento(event.getHorarioEvento());
			atualizarEvento.setDescricaoEvento(event.getDescricaoEvento());
			atualizarEvento.setHorarioEvento(event.getHorarioEvento());
			eventRepo.save(atualizarEvento);
			return atualizarEvento;
		}

		return null;
	}

	// Deletar Evento
	public Event deletarEvent(Long id) {
		Optional<Event> existEvent = eventRepo.findById(id);
		if (existEvent.isPresent()) {
			Event deletarEvent = existEvent.get();
			eventRepo.delete(deletarEvent);
			return deletarEvent;
		}

		return null;
	}

}
