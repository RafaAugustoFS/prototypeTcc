package com.onAcademy.tcc.model;

import java.util.Date;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	@ManyToMany(mappedBy = "turmaDisciplinas")
    private List<Discipline> turmaDisciplinas;  
	
	@ManyToMany(mappedBy = "classes") 
	private List<Teacher> teachers;

	
	@OneToMany(mappedBy = "classSt")
	private List<Student> students;
	
}
