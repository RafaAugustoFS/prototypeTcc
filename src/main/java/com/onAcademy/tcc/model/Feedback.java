package com.onAcademy.tcc.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	private Long createdBy;
	@ManyToOne
	@JoinColumn(name = "feedback_id")
	private Teacher recipientTeacher;
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student recipientStudent;
	
	@ManyToOne
	@JoinColumn(name = "classSt_id")
	private ClassSt classSt;
}
