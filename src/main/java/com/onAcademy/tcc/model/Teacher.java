package com.onAcademy.tcc.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.onAcademy.tcc.dto.StudentClassDTO;

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
	public static final String ENROLLMENT_PREFIX = "p";
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
        return ENROLLMENT_PREFIX + year + teacher.getNomeDocente().toLowerCase();
    }
	
	
	
	@PostPersist
	public void generateIdentifierCode() {
		String year = String.valueOf(LocalDate.now().getYear());
		String teacherId = String.format("%04d", id);
		String initials = (nomeDocente != null && nomeDocente.replaceAll("[^A-Za-z]", "").length() > 0)
				? nomeDocente.replaceAll("[^A-Za-z]", "").substring(0, Math.min(2, nomeDocente.length())).toUpperCase()
				: "XX";
		
		this.identifierCode = ENROLLMENT_PREFIX + year + teacherId+ initials;

	}
	
}
