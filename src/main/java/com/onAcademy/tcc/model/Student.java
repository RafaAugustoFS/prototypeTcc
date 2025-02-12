package com.onAcademy.tcc.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

		@OneToMany(mappedBy = "recipientStudent")
		private List<FeedbackByTeacher> feedback;
		
		@OneToMany(mappedBy = "studentId")
		@JsonManagedReference 
		private List<Note>notas;
		
		@OneToMany(mappedBy = "createdBy")
		private List<FeedBackByStudent> feedbackAluno;
		
		private Long turmaId;
		
		@ManyToOne
		@JoinColumn(name = "turmaId", insertable = false, updatable = false)
		private ClassSt classSt;
}
