package com.onAcademy.tcc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * Representa a entidade "FeedbackByTeacher" no banco de dados.
 * 
 * - Mapeia as informações sobre o feedback fornecido por um professor a um
 * aluno, incluindo o conteúdo do feedback e as relações com o professor, aluno
 * e turma.
 * 
 * A classe contém os seguintes campos: - id: O identificador único do feedback,
 * gerado automaticamente pelo banco de dados. - conteudo: O conteúdo do
 * feedback fornecido pelo professor. - createdBy: O professor que criou o
 * feedback. - recipientStudent: O aluno que recebeu o feedback. - classSt: A
 * turma associada ao feedback.
 * 
 * Relacionamentos: - A classe possui um relacionamento Many-to-One com a
 * entidade "Teacher", indicando o professor que criou o feedback. - A classe
 * possui um relacionamento Many-to-One com a entidade "Student", indicando o
 * aluno que recebeu o feedback. - A classe possui um relacionamento Many-to-One
 * com a entidade "ClassSt", indicando a turma associada ao feedback.
 * 
 * Essa classe é persistida no banco de dados e usa as anotações JPA para mapear
 * seus campos e relacionamentos.
 * 
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.ManyToOne
 * @see jakarta.persistence.JoinColumn
 * @see lombok.Data
 */

@Data
@Entity
public class FeedbackByTeacher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String conteudo;
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher createdBy;
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student recipientStudent;
	@ManyToOne
	@JoinColumn(name = "classSt_id")
	private ClassSt classSt;
}
