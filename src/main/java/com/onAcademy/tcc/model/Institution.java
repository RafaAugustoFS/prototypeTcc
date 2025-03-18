package com.onAcademy.tcc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa a entidade "Institution" no banco de dados.
 * 
 * - Mapeia as informações sobre a instituição, incluindo nome, código de
 * identificação, unidade e senha.
 * 
 * A classe contém os seguintes campos: - id: O identificador único da
 * instituição, gerado automaticamente pelo banco de dados. - nameInstitution: O
 * nome da instituição. - identifierCode: O código de identificação único da
 * instituição. - unitInstitution: A unidade da instituição (por exemplo,
 * "Campus Central", "Unidade 1"). - password: A senha associada à instituição.
 * 
 * Essa classe é persistida no banco de dados e usa as anotações JPA para mapear
 * seus campos.
 * 
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.GeneratedValue
 * @see jakarta.persistence.GenerationType
 * @see jakarta.persistence.Column
 * @see lombok.Data
 * @see lombok.NoArgsConstructor
 */

@Entity
@Data
@NoArgsConstructor
public class Institution {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nameInstitution;

	@Column(unique = true)
	private String identifierCode;

	private String unitInstitution;

	private String password;
}
