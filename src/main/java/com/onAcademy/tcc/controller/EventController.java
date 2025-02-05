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

import com.onAcademy.tcc.model.Event;

import com.onAcademy.tcc.service.EventService;

@RestController
@RequestMapping("/api")
public class EventController {

	@Autowired
	private EventService eventService;

	@PostMapping("/event")
	public ResponseEntity<Event> criarEvento(@RequestBody Event event) {
		Event event1 = eventService.criarEvento(event);
		return new ResponseEntity<>(event1, HttpStatus.CREATED);
	}

	@GetMapping("/event")
	public ResponseEntity<List<Event>> buscarTodosEventos() {
		List<Event> events = eventService.buscarEventos();
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@GetMapping("/event/{id}")
	public ResponseEntity<Event> buscarUnico(@PathVariable Long id) {

		Event eventUnico = eventService.buscarEventoUnico(id);
		if (eventUnico != null) {
			return new ResponseEntity<>(eventUnico, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/event/{id}")
	public ResponseEntity<Event> atualizarEvento(@PathVariable Long id, @RequestBody Event event) {
		Event atualizarEvento = eventService.atualizarEvento(id, event);
		if (atualizarEvento != null) {
			return new ResponseEntity<>(atualizarEvento, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/event/{id}")
	public ResponseEntity<Event> deletarEvento(@PathVariable Long id) {
		Event deletarEvent = eventService.deletarEvent(id);
		if (deletarEvent != null) {
			return new ResponseEntity<>(deletarEvent, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
