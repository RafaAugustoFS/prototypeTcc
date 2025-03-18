package com.onAcademy.tcc.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.Event;
import com.onAcademy.tcc.repository.EventRepo;

/**
 * Serviço responsável por operações relacionadas a eventos.
 */
@Service
public class EventService {

    @Autowired
    private EventRepo eventRepo;

    /**
     * Cria um novo evento.
     *
     * @param event O evento a ser criado.
     * @return O evento salvo.
     */
    public Event criarEvento(Event event) {
        return eventRepo.save(event);
    }

    /**
     * Retorna uma lista de todos os eventos cadastrados.
     *
     * @return Lista de eventos.
     */
    public List<Event> buscarEventos() {
        return eventRepo.findAll();
    }

    /**
     * Busca um evento pelo seu ID.
     *
     * @param id O ID do evento a ser buscado.
     * @return O evento encontrado ou null se não existir.
     */
    public Event buscarEventoUnico(Long id) {
        return eventRepo.findById(id).orElse(null);
    }

    /**
     * Atualiza os dados de um evento existente.
     *
     * @param id    O ID do evento a ser atualizado.
     * @param event Objeto contendo os novos dados do evento.
     * @return O evento atualizado ou null se o evento não existir.
     */
    public Event atualizarEvento(Long id, Event event) {
        return eventRepo.findById(id)
                .map(existingEvent -> {
                    existingEvent.setTituloEvento(event.getTituloEvento());
                    existingEvent.setLocalEvento(event.getLocalEvento());
                    existingEvent.setDataEvento(event.getDataEvento());
                    existingEvent.setHorarioEvento(event.getHorarioEvento());
                    existingEvent.setDescricaoEvento(event.getDescricaoEvento());
                    return eventRepo.save(existingEvent);
                })
                .orElse(null);
    }

    /**
     * Remove um evento pelo seu ID.
     *
     * @param id O ID do evento a ser removido.
     * @return O evento removido ou null se o evento não existir.
     */
    public Event deletarEvent(Long id) {
        return eventRepo.findById(id)
                .map(existingEvent -> {
                    eventRepo.delete(existingEvent);
                    return existingEvent;
                })
                .orElse(null);
    }
}