package com.onAcademy.tcc.dto;

import java.sql.Date;

import lombok.Data;

/**
 * Classe DTO (Data Transfer Object) que representa os dados de um aluno em uma turma.
 * Este objeto é utilizado para transferir as informações de um aluno, como dados pessoais, 
 * informações de contato, credenciais de login e detalhes da turma à qual ele pertence.
 * @see lombok.Data
 */


@Data
public class StudentClassDTO {
	private String nomeAluno;
	private Date dataNascimentoAluno;
	private String emailAluno;
	private String telefoneAluno;
	private String identifierCode;
	private String password;
	private Long turmaId;
	private String imageUrl;

}
