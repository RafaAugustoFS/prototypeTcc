package com.onAcademy.tcc.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;
@Data
public class TeacherDTO {
	String nomeDocente;
	Date dataNascimentoDocente;
	String emailDocente;
	String telefoneDocente;
	String identifierCode;
	String password;
	List<Long> disciplineId;
	String imageUrl;

}
