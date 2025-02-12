package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.FeedbackByTeacher;
import com.onAcademy.tcc.repository.FeedbackByTeacherRepo;

@Service
public class FeedbackByTeacherService {
	@Autowired
	private FeedbackByTeacherRepo feedbackByTeacherRepo;
	
	public FeedbackByTeacher criarFeedback(FeedbackByTeacher feedback) {
		FeedbackByTeacher criarFeedback = feedbackByTeacherRepo.save(feedback);
		return criarFeedback;
	}
	public List<FeedbackByTeacher> buscarTodosFeedbacks(){
		List<FeedbackByTeacher> buscarFeedbacks = feedbackByTeacherRepo.findAll();
		return buscarFeedbacks;
	}
	public FeedbackByTeacher buscarFeedbackUnico(Long id) {
		Optional<FeedbackByTeacher> existFeedback = feedbackByTeacherRepo.findById(id);
		if(existFeedback.isPresent()) {
			return existFeedback.get();
		}
		return null;
	}
}
