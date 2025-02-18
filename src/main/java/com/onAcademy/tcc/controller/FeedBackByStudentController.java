package com.onAcademy.tcc.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.onAcademy.tcc.model.FeedBackByStudent;
import com.onAcademy.tcc.service.FeedbackByStudentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Feedback", description = "EndPoint de feedback")
@RestController
@RequestMapping("/api")
public class FeedBackByStudentController {
	@Autowired
	private FeedbackByStudentService feedbackByStudentService;

	record TeacherDTO(String nomeDocente, Long id) {
	};

	record FeedbackDTO(String titulo, String conteudo, CreatedByDTO createdBy, TeacherDTO teacher) {
	};

	record CreatedByDTO(String nomeAluno, Long id) {
	};

	@PostMapping("/feedbackStudent")
	public ResponseEntity<FeedBackByStudent> criarFeedback(@RequestBody FeedBackByStudent feedbackByStudent) {
		FeedBackByStudent feedback1 = feedbackByStudentService.criarFeedbackStudent(feedbackByStudent);
		return new ResponseEntity<>(feedback1, HttpStatus.OK);
	}

	@GetMapping("/feedbackStudent")
	public ResponseEntity<List<FeedBackByStudent>> buscarTodasFeedback() {
		List<FeedBackByStudent> feedback = feedbackByStudentService.buscarTodosFeedbacksStudent();
		return new ResponseEntity<>(feedback, HttpStatus.OK);
	}

	@GetMapping("/feedbackStudent/{id}")
	public ResponseEntity<FeedbackDTO> buscarFeedback(@PathVariable Long id) {
		FeedBackByStudent buscarFeedback = feedbackByStudentService.buscarFeedbackUnicoStudent(id);
		var createdBy = new CreatedByDTO(buscarFeedback.getCreatedBy().getNomeAluno(),
				buscarFeedback.getCreatedBy().getId());
		var teacher = new TeacherDTO(buscarFeedback.getRecipientTeacher().getNomeDocente(), buscarFeedback.getId());

		var feedbackDTO = new FeedbackDTO(buscarFeedback.getTitulo(), buscarFeedback.getConteudo(), createdBy, teacher);
		if (buscarFeedback != null) {
			return new ResponseEntity<>(feedbackDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
