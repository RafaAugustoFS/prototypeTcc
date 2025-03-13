package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.onAcademy.tcc.model.FeedbackForm;
import com.onAcademy.tcc.repository.FeedbackFormRepo;

@Service
public class FeedbackFormService {
	@Autowired
	FeedbackFormRepo feedbackFormRepo;

	public FeedbackForm criarFeedbackStudent(FeedbackForm feedbackForm) {

		if (feedbackForm.getBimestre() > 4 || feedbackForm.getBimestre() < 1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bimestre inválido. Deve estar entre 1 e 4.");
		}

		boolean feedbackExists = feedbackFormRepo.existsByCreatedByAndRecipientStudentAndBimestre(
				feedbackForm.getCreatedBy(), feedbackForm.getRecipientStudent(), feedbackForm.getBimestre());

		if (feedbackExists) {
			throw new RuntimeException("Já existe um feedback para este aluno nesse bimestre.");
		}

		return feedbackFormRepo.save(feedbackForm);
	}

	public List<FeedbackForm> buscarTodosFeedbacksStudent() {
		List<FeedbackForm> buscarTodosFeedbacks = feedbackFormRepo.findAll();
		return buscarTodosFeedbacks;
	}

	public FeedbackForm buscarFeedbackUnico(Long id) {
		Optional<FeedbackForm> existFeedback = feedbackFormRepo.findById(id);
		if (existFeedback.isPresent()) {
			return existFeedback.get();
		}
		return null;
	}

	public List<FeedbackForm> buscarFeedbackPorAluno(Long id) {
		return feedbackFormRepo.findByRecipientStudentId(id);
	}

}
