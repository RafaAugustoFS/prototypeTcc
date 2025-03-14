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
	    if (createdBy != null && createdBy.getNomeDocente() != null && createdBy.getNomeDocente().replaceAll("[^A-Za-z]", "").length() >= 2) {
	        return createdBy.getNomeDocente().replaceAll("[^A-Za-z]", "").substring(0, 2).toUpperCase();
	    } else if (createdByInstitution != null) {
	        return "I";
	    }
	    return "XX";
	}

}
