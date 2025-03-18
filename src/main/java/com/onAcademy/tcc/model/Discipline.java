package com.onAcademy.tcc.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa a entidade "Disciplina" no banco de dados.
 * 
 * - Mapeia as informações sobre as disciplinas oferecidas no sistema. - Cada
 * disciplina pode estar associada a várias turmas e a vários professores.
 * 
 * A classe contém os seguintes campos: - id: O identificador único da
 * disciplina, gerado automaticamente pelo banco de dados. - nomeDisciplina: O
 * nome da disciplina. - turmaDisciplinas: A lista de turmas associadas a esta
 * disciplina. - teachers: A lista de professores que lecionam esta disciplina.
 * 
 * Relacionamentos: - A disciplina possui um relacionamento Many-to-Many com a
 * entidade "ClassSt", representando as turmas que oferecem essa disciplina. - A
 * disciplina possui um relacionamento Many-to-Many com a entidade "Teacher",
 * representando os professores que lecionam a disciplina.
 * 
 * Essa classe é persistida no banco de dados e usa as anotações JPA para mapear
 * seus campos e relacionamentos.
 * 
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.ManyToMany
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
public class Discipline {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nomeDisciplina;

	@ManyToMany(mappedBy = "disciplinaTurmas")
	private List<ClassSt> turmaDisciplinas;

	@ManyToMany(mappedBy = "disciplines")
	private List<Teacher> teachers;

}
