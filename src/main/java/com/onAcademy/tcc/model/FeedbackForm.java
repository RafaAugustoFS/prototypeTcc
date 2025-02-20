package com.onAcademy.tcc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class FeedbackForm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int resposta1;
	private int resposta2;
	private int resposta3;
	private int resposta4;
	private int resposta5;
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher createdBy;
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student recipientStudent;
}
