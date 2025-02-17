package com.onAcademy.tcc.dto;

import lombok.Data;

@Data
public class NoteDTO {
	private Long studentId;
	private Double nota;
	private int bimestre;
	private String status;
	private Long disciplineId;
}
