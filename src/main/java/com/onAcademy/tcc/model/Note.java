package com.onAcademy.tcc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * Representa uma nota de um estudante em uma disciplina.
 * Esta entidade armazena a nota, o bimestre ao qual ela se refere, o status da nota
 * e os relacionamentos com o estudante e a disciplina.
 *
 * @param id          Identificador único da nota.
 * @param student     Estudante associado à nota.
 * @param bimestre    Bimestre ao qual a nota se refere.
 * @param nota        Valor da nota.
 * @param status      Status da nota (ex: "Aprovado", "Reprovado", "Recuperação").
 * @param discipline  Disciplina associada à nota.
 */
@Entity
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Student studentId;

    private int bimestre;
    private Double nota;
    private String status;

    @ManyToOne
    Discipline disciplineId;;
}