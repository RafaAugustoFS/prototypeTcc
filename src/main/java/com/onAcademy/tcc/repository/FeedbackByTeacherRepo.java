package com.onAcademy.tcc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.FeedBackByStudent;
import com.onAcademy.tcc.model.FeedbackByTeacher;

/**
 * Repositório para a entidade "FeedbackByTeacher".
 * 
 * - Estende a interface JpaRepository, fornecendo os métodos básicos de
 * persistência (salvar, buscar, atualizar, excluir) para a entidade
 * `FeedbackByTeacher`. - A interface `FeedbackByTeacherRepo` não requer
 * implementação explícita, pois o Spring Data JPA fornece a implementação
 * automaticamente em tempo de execução.
 * 
 * A interface oferece os seguintes recursos: - **Crud básico**: Métodos como
 * `save()`, `findById()`, `findAll()`, `deleteById()` estão disponíveis por
 * herança da `JpaRepository`.
 * 
 * A principal finalidade desta interface é permitir o acesso à tabela que
 * armazena os dados de feedbacks dados pelos professores, permitindo a execução
 * de operações de CRUD (Create, Read, Update, Delete) de forma simplificada.
 * 
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.onAcademy.tcc.model.FeedbackByTeacher
 */

public interface FeedbackByTeacherRepo extends JpaRepository<FeedbackByTeacher, Long> {

	/**
	 * Método personalizado para buscar feedbacks dados por professores para um
	 * determinado estudante, baseado no ID do estudante.
	 * 
	 * @param RecipientStudentId ID do estudante destinatário do feedback
	 * @return Lista de feedbacks dados ao estudante identificado
	 */
	List<FeedbackByTeacher> findByRecipientStudent_Id(Long RecipientStudentId);
}
