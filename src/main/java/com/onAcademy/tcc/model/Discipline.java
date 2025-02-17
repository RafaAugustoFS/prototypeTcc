package com.onAcademy.tcc.model;

import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
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

	
	@ManyToMany()
	@JoinTable(
	        name = "class_discipline", 
	        joinColumns = { @JoinColumn(name = "discipline_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "class_id" ) }
	    )
	private List<ClassSt> turmaDisciplinas;
	 
	 
	@ManyToMany(mappedBy = "disciplines")
	private List<Teacher> teachers;
	
	
	
	
}
