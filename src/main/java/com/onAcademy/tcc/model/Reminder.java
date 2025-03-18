package com.onAcademy.tcc.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;

/**
 * Representa a entidade "Reminder" no banco de dados.
 * 
 * - Mapeia as informações sobre os lembretes criados no sistema, incluindo
 * conteúdo, data e hora de criação, instituição, professor e turma associada.
 * 
 * A classe contém os seguintes campos: - id: O identificador único do lembrete,
 * gerado automaticamente pelo banco de dados. - conteudo: O conteúdo ou
 * descrição do lembrete. - horarioSistema: O horário em que o lembrete foi
 * criado, preenchido automaticamente com a hora do sistema. -
 * createdByInstitution: A instituição que criou o lembrete. - createdBy: O
 * professor que criou o lembrete. - classSt: A turma associada ao lembrete.
 * 
 * Relacionamentos: - A classe possui um relacionamento Many-to-One com a
 * entidade "Institution", indicando a instituição que criou o lembrete. - A
 * classe possui um relacionamento Many-to-One com a entidade "Teacher",
 * indicando o professor que criou o lembrete. - A classe possui um
 * relacionamento Many-to-One com a entidade "ClassSt", indicando a turma
 * associada ao lembrete.
 * 
 * Métodos: - `prePersist`: Define automaticamente o horário de criação do
 * lembrete, se não fornecido, para o horário atual do sistema. - `getInitials`:
 * Retorna as iniciais do professor que criou o lembrete ou, se não houver,
 * retorna um valor padrão.
 * 
 * Essa classe é persistida no banco de dados e usa as anotações JPA para mapear
 * seus campos e relacionamentos.
 * 
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.GeneratedValue
 * @see jakarta.persistence.GenerationType
 * @see jakarta.persistence.ManyToOne
 * @see jakarta.persistence.JoinColumn
 * @see jakarta.persistence.Column
 * @see jakarta.persistence.PrePersist
 * @see lombok.Data
 */

@Entity
@Data
public class Reminder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String conteudo;
	@Column(nullable = false, updatable = false)
	private LocalDateTime horarioSistema;
	@ManyToOne
	@JoinColumn(name = "institution_id")
	private Institution createdByInstitution;
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher createdBy;
	@ManyToOne
	@JoinColumn(name = "class_st_id")
	private ClassSt classSt;

	@PrePersist
	public void prePersist() {
		if (this.horarioSistema == null) {
			this.horarioSistema = LocalDateTime.now(); // Definir a hora do sistema
		}
	}

	public String getInitials() {
		if (createdBy != null && createdBy.getNomeDocente() != null
				&& createdBy.getNomeDocente().replaceAll("[^A-Za-z]", "").length() >= 2) {
			return createdBy.getNomeDocente().replaceAll("[^A-Za-z]", "").substring(0, 2).toUpperCase();
		} else if (createdByInstitution != null) {
			return "I";
		}
		return "XX";
	}

}
