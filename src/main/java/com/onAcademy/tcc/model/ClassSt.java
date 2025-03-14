package com.onAcademy.tcc.model;


import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class ClassSt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nomeTurma;
	private Date anoLetivoTurma;
	private String periodoTurma;
	private int capacidadeMaximaTurma;
	private int salaTurma;

	@OneToMany(mappedBy = "classSt")
	private List<FeedbackByTeacher> feedback;

	@ManyToMany()
	@JoinTable(name = "class_discipline", joinColumns = { @JoinColumn(name = "class_id") }, inverseJoinColumns = {
			@JoinColumn(name = "discipline_id") })
	private List<Discipline> disciplinaTurmas;

	@ManyToMany
	@JoinTable(name = "class_teacher", joinColumns = { @JoinColumn(name = "classSt_id") }, inverseJoinColumns = {
			@JoinColumn(name = "teacher_id") })
	private List<Teacher> classes;

	@OneToMany(mappedBy = "classSt")
	private List<Student> students;

	@OneToMany(mappedBy = "classSt")
	private List<Reminder> reminder;
}
