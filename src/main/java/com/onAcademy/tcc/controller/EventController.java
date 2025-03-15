package com.onAcademy.tcc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.onAcademy.tcc.model.Event;
import com.onAcademy.tcc.service.EventService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Event", description = "EndPoint de evento")
@RestController
@RequestMapping("/api")
public class EventController {

	@Autowired
	private EventService eventService;

	/**
	 * Cria um novo evento.
	 *
	 * @param event Objeto contendo os dados do evento.
	 * @return ResponseEntity com o evento criado ou uma mensagem de erro.
	 */
	@PostMapping("/event")
	@PreAuthorize("hasRole('INSTITUTION')")
	public ResponseEntity<?> criarEvento(@RequestBody Event event) {
		try {
			validarEvento(event);
			Event eventoCriado = eventService.criarEvento(event);
			return new ResponseEntity<>(eventoCriado, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao criar evento: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Retorna uma lista de todos os eventos.
	 *
	 * @return ResponseEntity com a lista de eventos ou uma mensagem de erro.
	 */
	@GetMapping("/event")
	public ResponseEntity<?> buscarTodosEventos() {
		try {
			List<Event> eventos = eventService.buscarEventos();
			return new ResponseEntity<>(eventos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar eventos: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca um evento pelo ID.
	 *
	 * @param id ID do evento a ser buscado.
	 * @return ResponseEntity com o evento encontrado ou uma mensagem de erro.
	 */
	@GetMapping("/event/{id}")
	public ResponseEntity<?> buscarEventoPorId(@PathVariable Long id) {
		try {
			Event evento = eventService.buscarEventoUnico(id);
			if (evento == null) {
				return new ResponseEntity<>(Map.of("error", "Evento não encontrado"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(evento, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar evento: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Atualiza os dados de um evento existente.
	 *
	 * @param id    ID do evento a ser atualizado.
	 * @param event Objeto contendo os novos dados do evento.
	 * @return ResponseEntity com o evento atualizado ou uma mensagem de erro.
	 */
	@PutMapping("/event/{id}")
	public ResponseEntity<?> atualizarEvento(@PathVariable Long id, @RequestBody Event event) {
		try {
			validarEvento(event);
			Event eventoAtualizado = eventService.atualizarEvento(id, event);
			if (eventoAtualizado == null) {
				return new ResponseEntity<>(Map.of("error", "Evento não encontrado"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(eventoAtualizado, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao atualizar evento: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Exclui um evento pelo ID.
	 *
	 * @param id ID do evento a ser excluído.
	 * @return ResponseEntity com o evento excluído ou uma mensagem de erro.
	 */
	@DeleteMapping("/event/{id}")
	public ResponseEntity<?> deletarEvento(@PathVariable Long id) {
		try {
			Event eventoDeletado = eventService.deletarEvent(id);
			if (eventoDeletado == null) {
				return new ResponseEntity<>(Map.of("error", "Evento não encontrado"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(eventoDeletado, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao deletar evento: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Valida os dados de um evento.
	 *
	 * @param event Evento a ser validado.
	 * @throws IllegalArgumentException Se algum campo obrigatório estiver vazio ou
	 *                                  nulo.
	 */
	private void validarEvento(Event event) {
		if (event.getTituloEvento() == null || event.getTituloEvento().trim().isEmpty() || event.getDataEvento() == null
				|| event.getHorarioEvento() == null || event.getLocalEvento() == null
				|| event.getLocalEvento().trim().isEmpty() || event.getDescricaoEvento() == null
				|| event.getDescricaoEvento().trim().isEmpty()) {
			throw new IllegalArgumentException("Por favor, preencha todos os campos obrigatórios.");
		}
	}
}