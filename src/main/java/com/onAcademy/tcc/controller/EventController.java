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

    @PostMapping("/event")
    @PreAuthorize("hasRole('INSTITUTION')")
    public ResponseEntity<?> criarEvento(@RequestBody Event event) {
        try {
        	validarEvent(event);
            Event event1 = eventService.criarEvento(event);
            return new ResponseEntity<>(event1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao criar evento: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/event")
    public ResponseEntity<?> buscarTodosEventos() {
        try {
            List<Event> events = eventService.buscarEventos();
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao buscar eventos: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    public void validarEvent(Event event) {
    	if(event.getTituloEvento().isEmpty() || event.getDataEvento() == null || event.getHorarioEvento() == null || event.getLocalEvento().isEmpty() || event.getDescricaoEvento().isEmpty()) {
    		throw new IllegalArgumentException("Por favor preencha todos os campos.");
    	}
    }
    @GetMapping("/event/{id}")
    public ResponseEntity<?> buscarUnico(@PathVariable Long id) {
        try {
            Event eventUnico = eventService.buscarEventoUnico(id);
            if (eventUnico == null) {
                return new ResponseEntity<>(Map.of("error", "Evento não encontrado"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(eventUnico, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao buscar evento: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/event/{id}")
    public ResponseEntity<?> atualizarEvento(@PathVariable Long id, @RequestBody Event event) {
        try {
            Event atualizarEvento = eventService.atualizarEvento(id, event);
            if (atualizarEvento == null) {
                return new ResponseEntity<>(Map.of("error", "Evento não encontrado"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(atualizarEvento, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao atualizar evento: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<?> deletarEvento(@PathVariable Long id) {
        try {
            Event deletarEvent = eventService.deletarEvent(id);
            if (deletarEvent == null) {
                return new ResponseEntity<>(Map.of("error", "Evento não encontrado"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(deletarEvent, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao deletar evento: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
