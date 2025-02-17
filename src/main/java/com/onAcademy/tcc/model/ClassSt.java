package com.onAcademy.tcc.model;

import java.util.Date;
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
	@JoinTable(
	        name = "class_discipline", 
	        joinColumns = { @JoinColumn(name = "class_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "discipline_id" ) }
	    )
	private List<Discipline> disciplinaTurmas;
	
	
	@ManyToMany(mappedBy = "classes") 
	private List<Teacher> teachers;

	
	@OneToMany(mappedBy = "classSt")
	private List<Student> students;
	
}
