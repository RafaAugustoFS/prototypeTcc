package com.onAcademy.tcc.repository;

import com.onAcademy.tcc.model.Reminder;
import com.onAcademy.tcc.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório para a entidade {@link Reminder}.
 * Esta interface estende {@link JpaRepository}, fornecendo métodos CRUD (Create, Read, Update, Delete)
 * e operações de paginação e ordenação para a entidade {@link Reminder}.
 * Além disso, inclui métodos personalizados para buscar lembretes por ID da turma e por professor criador.
 *
 * @see JpaRepository
 * @see Reminder
 */
public interface ReminderRepo extends JpaRepository<Reminder, Long> {

    /**
     * Busca todos os lembretes associados a uma turma específica.
     *
     * @param classStId ID da turma.
     * @return Uma lista de lembretes associados à turma.
     */
    List<Reminder> findByClassStId(Long classStId);

    /**
     * Busca todos os lembretes criados por um professor específico.
     *
     * @param teacher Professor que criou os lembretes.
     * @return Uma lista de lembretes criados pelo professor.
     */
    List<Reminder> findByCreatedBy(Teacher teacher);
}