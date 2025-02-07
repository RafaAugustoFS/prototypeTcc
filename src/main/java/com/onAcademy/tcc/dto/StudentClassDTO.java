package com.onAcademy.tcc.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class StudentClassDTO {

	private String nomeAluno;
	private Date dataNascimentoAluno;
	private String emailAluno;
	private String telefoneAluno;
	private String matriculaAluno;
	private String senhaAluno;
	private Long turmaId;

}
