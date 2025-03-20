package com.onAcademy.tcc.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.FeedBackByStudent;
import com.onAcademy.tcc.model.FeedbackByTeacher;
import com.onAcademy.tcc.repository.FeedbackByTeacherRepo;

/**
 * Serviço responsável por operações relacionadas a feedbacks de professores.
 */
@Service
public class FeedbackByTeacherService {

	@Autowired
	private FeedbackByTeacherRepo feedbackByTeacherRepo;

	/**
	 * Cria um novo feedback de professor.
	 *
	 * @param feedback O feedback a ser criado.
	 * @return O feedback salvo.
	 */
	public FeedbackByTeacher criarFeedback(FeedbackByTeacher feedback) {
		return feedbackByTeacherRepo.save(feedback);
	}

	/**
	 * Retorna uma lista de todos os feedbacks de professores cadastrados.
	 *
	 * @return Lista de feedbacks.
	 */
	public List<FeedbackByTeacher> buscarTodosFeedbacks() {
		return feedbackByTeacherRepo.findAll();
	}

	/**
	 * Busca um feedback de professor pelo seu ID.
	 *
	 * @param id O ID do feedback a ser buscado.
	 * @return O feedback encontrado ou null se não existir.
	 */
	public FeedbackByTeacher buscarFeedbackUnico(Long id) {
		return feedbackByTeacherRepo.findById(id).orElse(null);
	}

	public List<FeedbackByTeacher> buscarFeedbacksAluno(Long id) {
		return feedbackByTeacherRepo.findByRecipientStudent_Id(id);
	}

}