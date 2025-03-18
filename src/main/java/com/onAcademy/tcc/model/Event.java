package com.onAcademy.tcc.model;

import java.time.LocalDate;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Representa a entidade "Evento" no banco de dados.
 * 
 * - Mapeia as informações sobre os eventos registrados no sistema, como título,
 * data, horário, local e descrição.
 * 
 * A classe contém os seguintes campos: - id: O identificador único do evento,
 * gerado automaticamente pelo banco de dados. - tituloEvento: O título do
 * evento. - dataEvento: A data em que o evento ocorrerá. - horarioEvento: O
 * horário em que o evento ocorrerá. - localEvento: O local onde o evento será
 * realizado. - descricaoEvento: A descrição detalhada sobre o evento.
 * 
 * Essa classe é persistida no banco de dados e usa as anotações JPA para mapear
 * seus campos.
 * 
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.GeneratedValue
 * @see jakarta.persistence.GenerationType
 * @see lombok.Data
 */

@Entity
@Data
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String tituloEvento;

	private LocalDate dataEvento;
	private LocalTime horarioEvento;
	private String localEvento;
	private String descricaoEvento;
}
