package com.onAcademy.tcc.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.onAcademy.tcc.model.FeedBackByStudent;

/**
 * Repositório para a entidade {@link FeedBackByStudent}. Esta interface estende
 * {@link JpaRepository}, fornecendo métodos CRUD (Create, Read, Update, Delete)
 * e operações de paginação e ordenação para a entidade
 * {@link FeedBackByStudent}. Além disso, inclui um método personalizado para
 * buscar feedbacks por ID do professor recipiente.
 *
 * @see JpaRepository
 * @see FeedBackByStudent
 */
public interface FeedbackByStudentRepo extends JpaRepository<FeedBackByStudent, Long> {

	/**
	 * Busca todos os feedbacks associados a um professor específico.
	 *
	 * @param recipientTeacherId ID do professor recipiente.
	 * @return Uma lista de feedbacks associados ao professor.
	 */
	List<FeedBackByStudent> findByRecipientTeacher_Id(Long recipientTeacherId);
}