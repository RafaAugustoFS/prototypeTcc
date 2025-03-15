package com.onAcademy.tcc.model;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.onAcademy.tcc.dto.StudentClassDTO;

import jakarta.persistence.*;
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
	private Long turmaId;
	private String identifierCode;
	private String password;
	private String imageUrl;
	
	private static final String ENROLLMENT_PREFIX = "a";
	private static final int IDENTIFIER_CODE_LENGTH = 10; // Defina o comprimento desejado
	@OneToMany(mappedBy = "recipientStudent", fetch = FetchType.EAGER)
	private List<FeedbackByTeacher> feedback;

	@OneToMany(mappedBy = "studentId", fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<Note> notas;

	@OneToMany(mappedBy = "createdBy", fetch = FetchType.EAGER)
	private List<FeedBackByStudent> feedbackAluno;

	@ManyToOne
	@JoinColumn(name = "turmaId", insertable = false, updatable = false)
	private ClassSt classSt;

	@OneToMany(mappedBy = "recipientStudent", fetch = FetchType.EAGER)
	private List<FeedbackForm> feedbackForm;

	public static String generateRandomPassword(StudentClassDTO studentDTO, ClassSt classSt) {
        String year = String.valueOf(studentDTO.getDataNascimentoAluno().getYear());
        return ENROLLMENT_PREFIX + year + studentDTO.getNomeAluno().replaceAll("\\s", "").toLowerCase();
    }
	@PostPersist
	private void generateIdentifierCode() {
	    String numbers = "0123456789"; 
	    StringBuilder sb = new StringBuilder(); 
	    Random random = new Random(); 
	    for (int i = 0; i < IDENTIFIER_CODE_LENGTH; i++) {
	        
	        sb.append(numbers.charAt(random.nextInt(numbers.length())));
	    }
	    this.identifierCode = sb.toString();
	}

}
