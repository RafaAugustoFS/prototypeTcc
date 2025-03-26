package com.onAcademy.tcc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.FeedbackForm;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.model.Teacher;

/**
 * Repositório para a entidade {@link FeedbackForm}.
 * Esta interface estende {@link JpaRepository}, fornecendo métodos CRUD (Create, Read, Update, Delete)
 * e operações de paginação e ordenação para a entidade {@link FeedbackForm}.
 * Além disso, inclui métodos personalizados para verificar a existência de formulários de feedback
 * e buscar formulários por ID do estudante recipiente.
 */
public interface FeedbackFormRepo extends JpaRepository<FeedbackForm, Long> {

    /**
     * Verifica se já existe um formulário de feedback criado por um professor específico,
     * para um estudante específico e em um determinado bimestre.
     *
     * @param createdBy Professor que criou o formulário
     * @param recipientStudent Estudante recipiente do formulário
     * @param bimestre Bimestre ao qual o formulário se refere
     * @return true se o formulário existir, false caso contrário
     */
    boolean existsByCreatedByAndRecipientStudentAndBimestre(Teacher createdBy, Student recipientStudent, int bimestre);

    /**
     * Busca todos os formulários de feedback associados a um estudante específico.
     *
     * @param id ID do estudante recipiente
     * @return Lista de formulários de feedback associados ao estudante
     */
    List<FeedbackForm> findByRecipientStudentId(Long id);
    
    /**
     * Busca todos os feedbacks de alunos de uma turma específica.
     * 
     * @param classId ID da turma (classSt)
     * @return Lista de feedbacks dos alunos da turma
     */
    List<FeedbackForm> findByRecipientStudentTurmaId(Long turmaId);
    
       
}