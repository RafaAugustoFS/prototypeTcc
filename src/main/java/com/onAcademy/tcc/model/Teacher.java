package com.onAcademy.tcc.model;

import java.util.Date;
import java.util.List;
import java.util.Random;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostPersist;
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
	private String identifierCode;
	private String password;
	private String imageUrl;
	public static final String ENROLLMENT_PREFIX = "p";
	private static final int IDENTIFIER_CODE_LENGTH = 10; 
	@OneToMany(mappedBy = "recipientTeacher")
	private List<FeedBackByStudent> feedback;

	@ManyToMany
	@JoinTable(name = "teacher_discipline", joinColumns = { @JoinColumn(name = "teacher_id") }, inverseJoinColumns = {
			@JoinColumn(name = "discipline_id") })
	private List<Discipline> disciplines;

	@ManyToMany(mappedBy = "classes")
	private List<ClassSt> teachers;

	@OneToMany(mappedBy = "createdBy")
	private List<FeedbackByTeacher> feedbackProfessor;
	
	@OneToMany(mappedBy = "createdBy")
	private List<FeedbackForm> feedbackForm; 

	@OneToMany(mappedBy = "createdBy")
	private List<Reminder> reminder; 
	
	public static String generateRandomPassword(Teacher teacher) {
        String year = String.valueOf(teacher.getDataNascimentoDocente().getYear());
        return ENROLLMENT_PREFIX + year + teacher.getNomeDocente().replaceAll("\\s", "").toLowerCase();
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
