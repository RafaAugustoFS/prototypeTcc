package com.onAcademy.tcc.model;

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

/**
 * Representa a entidade "Student" no banco de dados.
 * 
 * - Mapeia as informações sobre o aluno, incluindo nome, data de nascimento,
 * e-mail, telefone, código de identificação, turma associada, feedbacks e
 * notas.
 * 
 * A classe contém os seguintes campos: - id: O identificador único do aluno,
 * gerado automaticamente pelo banco de dados. - nomeAluno: O nome completo do
 * aluno. - dataNascimentoAluno: A data de nascimento do aluno. - emailAluno: O
 * e-mail do aluno. - telefoneAluno: O número de telefone do aluno. - turmaId: O
 * ID da turma associada ao aluno. - identifierCode: O código de identificação
 * único do aluno, gerado aleatoriamente. - password: A senha associada ao
 * aluno, gerada com base em um padrão. - imageUrl: A URL da imagem de perfil do
 * aluno.
 * 
 * Relacionamentos: - A classe possui um relacionamento One-to-Many com a
 * entidade "FeedbackByTeacher", representando os feedbacks recebidos do
 * professor. - A classe possui um relacionamento One-to-Many com a entidade
 * "Note", representando as notas do aluno. - A classe possui um relacionamento
 * One-to-Many com a entidade "FeedBackByStudent", representando os feedbacks
 * dados pelo aluno. - A classe possui um relacionamento Many-to-One com a
 * entidade "ClassSt", indicando a turma à qual o aluno pertence. - A classe
 * possui um relacionamento One-to-Many com a entidade "FeedbackForm",
 * representando os formulários de feedback recebidos pelo aluno.
 * 
 * Métodos: - `generateRandomPassword`: Gera uma senha aleatória para o aluno
 * com base no nome e data de nascimento. - `generateIdentifierCode`: Gera um
 * código de identificação único para o aluno após a persistência no banco de
 * dados.
 * 
 * Essa classe é persistida no banco de dados e usa as anotações JPA para mapear
 * seus campos e relacionamentos.
 * 
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.GeneratedValue
 * @see jakarta.persistence.GenerationType
 * @see jakarta.persistence.OneToMany
 * @see jakarta.persistence.ManyToOne
 * @see jakarta.persistence.JoinColumn
 * @see jakarta.persistence.PostPersist
 * @see com.fasterxml.jackson.annotation.JsonManagedReference
 * @see lombok.Data
 * @see lombok.Getter
 * @see lombok.Setter
 * @see lombok.NoArgsConstructor
 */

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
	private static final int IDENTIFIER_CODE_LENGTH = 10;
	
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
		sb.append(ENROLLMENT_PREFIX);
		for (int i = 0; i < IDENTIFIER_CODE_LENGTH; i++) {

			sb.append(numbers.charAt(random.nextInt(numbers.length())));
		}

		this.identifierCode = sb.toString();
	}

}
