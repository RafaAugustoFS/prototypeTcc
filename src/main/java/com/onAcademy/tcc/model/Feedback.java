package com.onAcademy.tcc.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Feedback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private String conteudo;
	private Long CreatedBy;
	// @OneToMany
	// private ClassSt idTurma;
	@OneToMany(mappedBy = "feedback")
	private List<FeedbackStudent> feedbackAluno;
	@OneToMany(mappedBy = "feedback")
	private List<FeedbackTeacher> feedbackProfessor;
}
