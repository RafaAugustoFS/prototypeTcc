package com.onAcademy.tcc.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Student {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		private String nomeAluno;
		private Date dataNascimentoAluno;
		private String emailAluno;
		private String telefoneAluno;
		private String matriculaAluno;
		private String senhaAluno;
		@OneToMany
		private List<Note>notas;
		@OneToMany(mappedBy = "student")
		private List<FeedbackStudent> feedbackAluno;
}
