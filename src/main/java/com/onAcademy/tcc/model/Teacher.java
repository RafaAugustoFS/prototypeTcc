package com.onAcademy.tcc.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private String matriculaDocente;
	@OneToMany(mappedBy = "teacher")
	private List<DisciplineTeacher> disciplineTeachers;
	
	@OneToMany(mappedBy = "teacher")
	private List<ClassTeacher>classTeachers;
	
	@OneToMany(mappedBy = "teacher")
	private List<FeedbackTeacher> feedbackProfessor;
}
