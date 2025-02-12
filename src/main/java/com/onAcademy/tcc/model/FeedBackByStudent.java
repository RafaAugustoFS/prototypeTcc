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
public class FeedBackByStudent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private String conteudo;
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student createdBy;
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher recipientTeacher;

}
