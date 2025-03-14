package com.onAcademy.tcc.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onAcademy.tcc.model.FeedBackByStudent;
import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.service.FeedbackByStudentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Feedback", description = "EndPoint de feedback")
@RestController
@RequestMapping("/api")
public class FeedBackByStudentController {

	@Autowired
	private FeedbackByStudentService feedbackByStudentService;

	record TeacherDTO(String nomeDocente, Long id) {
	}

	record CreatedByDTO(String nomeAluno, Long id) {
	}

	record FeedbackDTO(String conteudo, CreatedByDTO createdBy, TeacherDTO teacher) {
	}

	record RecipientDTO(Long id, TeacherDTO teacher, String conteudo) {
	}

	@PostMapping("/feedbackStudent")
	public ResponseEntity<?> criarFeedback(@RequestBody FeedBackByStudent feedbackByStudent) {
		try {
			validarFeedBackByStudent(feedbackByStudent);
			FeedBackByStudent feedback1 = feedbackByStudentService.criarFeedbackStudent(feedbackByStudent);
			return new ResponseEntity<>(feedback1, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao criar feedback: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	public void validarFeedBackByStudent(FeedBackByStudent feedbackByStudent) {
		if (feedbackByStudent.getConteudo().isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha todos os campos.");
		}
	}

	@GetMapping("/feedbackStudent")
	public ResponseEntity<?> buscarTodasFeedback() {
		try {
			List<FeedBackByStudent> feedbacks = feedbackByStudentService.buscarTodosFeedbacksStudent();
			return new ResponseEntity<>(feedbacks, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar feedbacks: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/feedbackStudent/{id}")
	public ResponseEntity<?> buscarFeedback(@PathVariable Long id) {
		try {
			FeedBackByStudent buscarFeedback = feedbackByStudentService.buscarFeedbackUnicoStudent(id);

			if (buscarFeedback == null) {
				return new ResponseEntity<>(Map.of("error", "Feedback não encontrado"), HttpStatus.NOT_FOUND);
			}

			var createdBy = new CreatedByDTO(buscarFeedback.getCreatedBy().getNomeAluno(),
					buscarFeedback.getCreatedBy().getId());
			var teacher = new TeacherDTO(buscarFeedback.getRecipientTeacher().getNomeDocente(),
					buscarFeedback.getRecipientTeacher().getId());
			var feedbackDTO = new FeedbackDTO( buscarFeedback.getConteudo(), createdBy,
					teacher);

			return new ResponseEntity<>(feedbackDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar feedback: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("feedbackStudent/teacher/{recipientTeacherId}")
	public ResponseEntity<List<RecipientDTO>> buscarFeedbacksDocente(@PathVariable Long recipientTeacherId) {
		List<FeedBackByStudent> feedbacks = feedbackByStudentService.buscarFeedbacksDocente(recipientTeacherId);

		if (feedbacks.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<RecipientDTO> recipientDTO = feedbacks.stream().map(feedback -> {
			TeacherDTO teacherDTO = new TeacherDTO(
					feedback.getRecipientTeacher() != null ? feedback.getRecipientTeacher().getNomeDocente() : null,
					feedback.getRecipientTeacher() != null ? feedback.getRecipientTeacher().getId() : null);
			return new RecipientDTO(feedback.getId(), teacherDTO, feedback.getConteudo());
		}).collect(Collectors.toList());

		return new ResponseEntity<>(recipientDTO, HttpStatus.OK);
	}
}
