package com.onAcademy.tcc.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

/**
 * Representa uma turma (classe) no sistema.
 * Esta entidade armazena informações sobre a turma, como nome, ano letivo, período,
 * capacidade máxima, sala, e relacionamentos com outras entidades como disciplinas,
 * professores, estudantes, feedbacks e lembretes.
 *
 * @param id                   Identificador único da turma.
 * @param nomeTurma            Nome da turma.
 * @param anoLetivoTurma       Ano letivo da turma.
 * @param periodoTurma         Período da turma (ex: "Manhã", "Tarde", "Noite").
 * @param capacidadeMaximaTurma Capacidade máxima de estudantes na turma.
 * @param salaTurma            Número da sala onde a turma ocorre.
 * @param feedback             Lista de feedbacks associados à turma.
 * @param disciplinaTurmas     Lista de disciplinas associadas à turma.
 * @param classes              Lista de professores associados à turma.
 * @param students             Lista de estudantes matriculados na turma.
 * @param reminder             Lista de lembretes associados à turma.
 */
@Entity
@Data
public class ClassSt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeTurma;
    private int anoLetivoTurma;
    private String periodoTurma;
    private int capacidadeMaximaTurma;
    private int salaTurma;

    @OneToMany(mappedBy = "classSt")
    private List<FeedbackByTeacher> feedback;

    @ManyToMany
    @JoinTable(
        name = "class_discipline",
        joinColumns = @JoinColumn(name = "class_id"),
        inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    private List<Discipline> disciplinaTurmas;

    @ManyToMany
    @JoinTable(
        name = "class_teacher",
        joinColumns = @JoinColumn(name = "classSt_id"),
        inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> classes;

    @OneToMany(
    	    mappedBy = "classSt",
    	    cascade = CascadeType.ALL,
    	    orphanRemoval = true
    	)
    private List<Student> students;
    
    @OneToMany(mappedBy = "classSt")
    private List<Reminder> reminder;
}