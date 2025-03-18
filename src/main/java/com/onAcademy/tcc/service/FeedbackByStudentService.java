package com.onAcademy.tcc.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.onAcademy.tcc.model.FeedBackByStudent;
import com.onAcademy.tcc.repository.FeedbackByStudentRepo;

/**
 * Serviço responsável por operações relacionadas a feedbacks de estudantes.
 */
@Service
public class FeedbackByStudentService {

	@Autowired
	private FeedbackByStudentRepo feedbackByStudentRepo;

	/**
	 * Cria um novo feedback de estudante.
	 *
	 * @param feedbackByStudent O feedback a ser criado.
	 * @return O feedback salvo.
	 */
	public FeedBackByStudent criarFeedbackStudent(FeedBackByStudent feedbackByStudent) {
		return feedbackByStudentRepo.save(feedbackByStudent);
	}

	/**
	 * Retorna uma lista de todos os feedbacks de estudantes cadastrados.
	 *
	 * @return Lista de feedbacks.
	 */
	public List<FeedBackByStudent> buscarTodosFeedbacksStudent() {
		return feedbackByStudentRepo.findAll();
	}

	/**
	 * Busca um feedback de estudante pelo seu ID.
	 *
	 * @param id O ID do feedback a ser buscado.
	 * @return O feedback encontrado ou null se não existir.
	 */
	public FeedBackByStudent buscarFeedbackUnicoStudent(Long id) {
		return feedbackByStudentRepo.findById(id).orElse(null);
	}

	/**
	 * Busca todos os feedbacks associados a um docente específico.
	 *
	 * @param recipientTeacher O ID do docente recipiente.
	 * @return Lista de feedbacks associados ao docente.
	 */
	public List<FeedBackByStudent> buscarFeedbacksDocente(Long recipientTeacher) {
		return feedbackByStudentRepo.findByRecipientTeacher_Id(recipientTeacher);
	}
}