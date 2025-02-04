package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.Feedback;
import com.onAcademy.tcc.repository.FeedbackRepo;

@Service
public class FeedbackService {
	@Autowired
	private FeedbackRepo feedbackRepo;
	
	public Feedback criarFeedback(Feedback feedback) {
		Feedback criarFeedback = feedbackRepo.save(feedback);
		return criarFeedback;
	}
	public List<Feedback> buscarTodosFeedbacks(){
		List<Feedback>buscarFeedbacks = feedbackRepo.findAll();
		return buscarFeedbacks;
	}
	public Feedback buscarFeedbackUnico(Long id) {
		Optional<Feedback> existFeedback = feedbackRepo.findById(id);
		if(existFeedback.isPresent()) {
			return existFeedback.get();
		}
		return null;
	}
	public Feedback atualizarFeedback(Long id, Feedback feedback) {
		Optional<Feedback> existFeedback = feedbackRepo.findById(id);
		if(existFeedback.isPresent()) {
			Feedback atualizarFeedback = existFeedback.get();
			atualizarFeedback.setTitulo(feedback.getTitulo());
			atualizarFeedback.setConteudo(feedback.getConteudo());
			feedbackRepo.save(atualizarFeedback);
			return atualizarFeedback;
		}
		return null;
	}
	public Feedback deletarFeedback(Long id) {
		Optional<Feedback> existFeedback = feedbackRepo.findById(id);
		if(existFeedback.isPresent()) {
			Feedback deletarFeedback = existFeedback.get();
			feedbackRepo.delete(deletarFeedback);
			return deletarFeedback;
		}
		return null;
	}
}
