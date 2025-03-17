package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.model.Note;
import com.onAcademy.tcc.model.Student;

/**
 * Repositório para a entidade {@link Note}.
 * Esta interface estende {@link JpaRepository}, fornecendo métodos CRUD (Create, Read, Update, Delete)
 * e operações de paginação e ordenação para a entidade {@link Note}.
 * Além disso, inclui um método personalizado para verificar a existência de uma nota
 * com base no estudante, disciplina e bimestre.
 *
 * @see JpaRepository
 * @see Note
 */
public interface NoteRepo extends JpaRepository<Note, Long> {

    /**
     * Verifica se já existe uma nota associada a um estudante específico,
     * em uma disciplina específica e em um determinado bimestre.
     *
     * @param studentId    Estudante associado à nota.
     * @param disciplineId Disciplina associada à nota.
     * @param bimestre     Bimestre ao qual a nota se refere.
     * @return `true` se a nota existir, `false` caso contrário.
     */
    boolean existsByStudentIdAndDisciplineIdAndBimestre(Student studentId, Discipline disciplineId, int bimestre);
}