package com.onAcademy.tcc.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
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
	private List<Feedback> feedback ;
	
	@OneToMany(mappedBy = "classSt")
    private List<ClassDiscipline> turmaDisciplinas;  
	
	
	@OneToMany(mappedBy = "classSt")
	private List<ClassTeacher> classTeachers;
	
	@OneToMany(mappedBy = "classSt")
	@JsonBackReference  
	private List<Student> students;
	
}
