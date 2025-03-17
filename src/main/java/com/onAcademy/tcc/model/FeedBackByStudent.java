package com.onAcademy.tcc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * Representa um feedback enviado por um estudante para um professor.
 * Esta entidade armazena o conteúdo do feedback e os relacionamentos com
 * o estudante que criou o feedback e o professor que o recebeu.
 *
 * @param id             Identificador único do feedback.
 * @param conteudo       Conteúdo do feedback.
 * @param createdBy      Estudante que criou o feedback.
 * @param recipientTeacher Professor que recebeu o feedback.
 */
@Entity
@Data
public class FeedBackByStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conteudo;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student createdBy;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher recipientTeacher;
}