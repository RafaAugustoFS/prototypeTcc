package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.FeedbackForm;
import com.onAcademy.tcc.repository.FeedbackFormRepo;

@Service
public class FeedbackFormService {
	@Autowired
	FeedbackFormRepo feedbackFormRepo;
	
	public FeedbackForm criarFeedbackStudent(FeedbackForm feedbackForm) {
		FeedbackForm criarFeedbackStudent = feedbackFormRepo.save(feedbackForm);
		return criarFeedbackStudent;
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
}
