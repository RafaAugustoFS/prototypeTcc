package com.onAcademy.tcc.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
@Entity
public class Teacher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nomeDocente;
	private Date dataNascimentoDocente;
	private String emailDocente;
	private String telefoneDocente;
	private String matriculaDocente;
	private List<Class>turmas;
	@OneToMany(mappedBy = "teacher")
	private List<DisciplineTeacher> discipline_idteach;
}
