package com.onAcademy.tcc.model;

import java.util.Date;
import java.util.List;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data

public class Teacher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nomeDocente;
	private Date dataNascimentoDocente;
	private String emailDocente;
	private String telefoneDocente;
	private String identifierCode;
	private String password;

	@OneToMany(mappedBy = "recipientTeacher")
	private List<FeedBackByStudent> feedback;

	@ManyToMany
	@JoinTable(name = "teacher_discipline", joinColumns = { @JoinColumn(name = "teacher_id") }, inverseJoinColumns = {
			@JoinColumn(name = "discipline_id") })
	private List<Discipline> disciplines;

	@ManyToMany
	@JoinTable(name = "class_teacher", joinColumns = { @JoinColumn(name = "teacher_id") }, inverseJoinColumns = {
			@JoinColumn(name = "classSt_id") })
	private List<ClassSt> classes;

	@OneToMany(mappedBy = "createdBy")
	private List<FeedbackByTeacher> feedbackProfessor;

}
