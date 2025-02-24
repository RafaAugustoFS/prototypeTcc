package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.onAcademy.tcc.model.FeedBackByStudent;
import com.onAcademy.tcc.repository.FeedbackByStudentRepo;

@Service
public class FeedbackByStudentService {
	@Autowired
	FeedbackByStudentRepo feedBackByStudentRepo;

	public FeedBackByStudent criarFeedbackStudent(FeedBackByStudent feedbackByStudent) {
		FeedBackByStudent criarFeedbackStudent = feedBackByStudentRepo.save(feedbackByStudent);
		return criarFeedbackStudent;
	}

	public List<FeedBackByStudent> buscarTodosFeedbacksStudent() {
		List<FeedBackByStudent> buscarTodosFeedbacksStudent = feedBackByStudentRepo.findAll();
		return buscarTodosFeedbacksStudent;
	}

	public FeedBackByStudent buscarFeedbackUnicoStudent(Long id) {
		Optional<FeedBackByStudent> existFeedback = feedBackByStudentRepo.findById(id);
		if (existFeedback.isPresent()) {
			return existFeedback.get();
		}
		return null;
	}
	public List<FeedBackByStudent> buscarFeedbacksDocente(Long recipientTeacher){
		return feedBackByStudentRepo.findByRecipientTeacher_Id(recipientTeacher);
	}

}
