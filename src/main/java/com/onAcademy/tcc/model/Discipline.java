package com.onAcademy.tcc.model;

import java.util.List;

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
public class Discipline {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nomeDisciplina;
	
	// @OneToMany(mappedBy = "discipline")
	// private List<ClassDiscipline> turmaDisciplina;
	 
	 
	@OneToMany(mappedBy = "discipline")
	private List<DisciplineTeacher> disciplinaProfessor;
	
}
