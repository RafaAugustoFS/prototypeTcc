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

import com.onAcademy.tcc.controller.FeedBackByStudentController.CreatedByDTO;
import com.onAcademy.tcc.controller.FeedBackByStudentController.FeedbackDTO;
import com.onAcademy.tcc.controller.FeedBackByStudentController.TeacherDTO;
import com.onAcademy.tcc.controller.FeedbackByTeacherController.StudentDTO;
import com.onAcademy.tcc.model.FeedBackByStudent;
import com.onAcademy.tcc.model.FeedbackForm;
import com.onAcademy.tcc.service.FeedbackFormService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "FeedbackForm", description = "EndPoint do formulário feedback")
@RestController
@RequestMapping("/api")
public class FeedbackFormController {
	@Autowired
	private FeedbackFormService feedbackFormService;
	
	record StudentDTO(String nomeAluno, Long id) {
	}

	record ClassStDTO(String nomeTurma, Long id) {
	}

	record CreatedByDTO(String nomeDocente, Long id) {
	}

	record FeedbackDTO(int resposta1, int resposta2,int resposta3,int resposta4,int resposta5, CreatedByDTO createdByDTO, StudentDTO student) {
	}
	
	@PostMapping("/feedbackForm")
	public ResponseEntity<FeedbackForm> criarFeedback(@RequestBody FeedbackForm feedbackByStudent) {
		FeedbackForm feedback1 = feedbackFormService.criarFeedbackStudent(feedbackByStudent);
		return new ResponseEntity<>(feedback1, HttpStatus.OK);
	}

	@GetMapping("/feedbackForm")
	public ResponseEntity<List<FeedbackForm>> buscarTodasFeedback() {
		List<FeedbackForm> feedback = feedbackFormService.buscarTodosFeedbacksStudent();
		return new ResponseEntity<>(feedback, HttpStatus.OK);
	}

	@GetMapping("/feedbackForm/{id}")
	public ResponseEntity<FeedbackDTO> buscarFeedback(@PathVariable Long id) {
		FeedbackForm buscarFeedback = feedbackFormService.buscarFeedbackUnico(id);
		if (buscarFeedback != null) {
			var teacher = new CreatedByDTO(buscarFeedback.getCreatedBy().getNomeDocente(),
					buscarFeedback.getCreatedBy().getId());
			var student = new StudentDTO(buscarFeedback.getRecipientStudent().getNomeAluno(),
					buscarFeedback.getRecipientStudent().getId());
			var feedbackDTO = new FeedbackDTO(buscarFeedback.getResposta1(), buscarFeedback.getResposta2(), buscarFeedback.getResposta3(), buscarFeedback.getResposta4(), buscarFeedback.getResposta5(), teacher, student);
			return new ResponseEntity<>(feedbackDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
