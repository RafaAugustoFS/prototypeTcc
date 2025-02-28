package com.onAcademy.tcc.model;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
	
	private static final String ENROLLMENT_PREFIX = "a";
	
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
        return ENROLLMENT_PREFIX + year + studentDTO.getNomeAluno().toLowerCase();
    }
	
	  @PostPersist
		public void generateIdentifierCode() {
			String year = String.valueOf(LocalDate.now().getYear());
			String studentId = String.format("%04d", id);
			String classCode = (turmaId != null) ? String.valueOf(turmaId) : "SNTF";

			String initials = (nomeAluno != null && nomeAluno.replaceAll("[^A-Za-z]", "").length() > 0)
					? nomeAluno.replaceAll("[^A-Za-z]", "").substring(0, Math.min(2, nomeAluno.length())).toUpperCase()
					: "XX";
			this.identifierCode = String.format(ENROLLMENT_PREFIX+ year + studentId+ classCode+ initials);

		}

}
