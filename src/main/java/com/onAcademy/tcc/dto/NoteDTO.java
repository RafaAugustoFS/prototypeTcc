package com.onAcademy.tcc.dto;

import lombok.Data;

/**
 * @param studentId    Identificador único do estudante.
 * @param nota         Valor da nota do estudante.
 * @param bimestre     Número do bimestre ao qual a nota pertence.
 * @param status       Status da nota (ex: "Aprovado", "Reprovado", "Recuperação").
 * @param disciplineId Identificador único da disciplina à qual a nota está associada.
 */
@Data
public class NoteDTO {
    private Long studentId;
    private Double nota;
    private int bimestre;
    private String status;
    private Long disciplineId;
}