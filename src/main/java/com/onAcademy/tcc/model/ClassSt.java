package com.onAcademy.tcc.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
@Entity
public class ClassSt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nomeTurma;
	private Date anoLetivoTurma;
	private String periodoTurma;
	private int capacidadeMaximaTurma;
	private String salaTurma;
	@OneToMany
	private List<Student> alunos;
	@OneToMany(mappedBy = "classSt")
    private List<ClassDiscipline> turmaDisciplinas;  // Relacionamento com a tabela intermediária
}
