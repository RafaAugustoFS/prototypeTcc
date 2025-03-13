package com.onAcademy.tcc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.model.FeedbackForm;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.repository.FeedbackFormRepo;
import com.onAcademy.tcc.repository.StudentRepo;
import com.onAcademy.tcc.service.FeedbackFormService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "FeedbackForm", description = "EndPoint do formulário feedback")
@RestController
@RequestMapping("/api")
public class FeedbackFormController {
	@Autowired
	private FeedbackFormService feedbackFormService;

	@Autowired
	FeedbackFormRepo feedbackFormRepo;

	@Autowired
	StudentRepo studentRepo;

	record StudentDTO(String nomeAluno, Long id) {
	}

	record CreatedByDTO(String nomeDocente, Long id) {
	}

	record FeedbackDTO(int resposta1, int resposta2, int resposta3, int resposta4, int resposta5,
			CreatedByDTO createdByDTO, StudentDTO student) {
	}

	@PostMapping("/feedbackForm")
	public ResponseEntity<?> criarFeedback(@RequestBody FeedbackForm feedbackByStudent) {

		try {
			validarFeedback(feedbackByStudent);

			FeedbackForm feedback1 = feedbackFormService.criarFeedbackStudent(feedbackByStudent);
			return ResponseEntity.status(HttpStatus.CREATED).body(feedback1);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("error", "Erro ao criar professor: " + e.getMessage()));
		}

	}

	private void validarFeedback(FeedbackForm feedbackByStudent) {
		Student student = studentRepo.findById(feedbackByStudent.getRecipientStudent().getId())
				.orElseThrow(() -> new RuntimeException("Estudante não encontrado."));
		if (feedbackByStudent.getRecipientStudent() == null) {
			new IllegalArgumentException("Precisa indicar para qual aluno é este feedback.");
		}

		if (feedbackByStudent.getBimestre() > 4 || feedbackByStudent.getBimestre() < 1) {
			throw new IllegalArgumentException("Bimestre inválido. Deve estar entre 1 e 4.");
		}
		boolean feedbackExists = feedbackFormRepo.existsByCreatedByAndRecipientStudentAndBimestre(
				feedbackByStudent.getCreatedBy(), feedbackByStudent.getRecipientStudent(),
				feedbackByStudent.getBimestre());
		if (feedbackExists) {
			throw new RuntimeException("Já existe um feedback para este aluno nesse bimestre.");
		}

	}

	@GetMapping("/student/feedback/{id}")
	public ResponseEntity<List<FeedbackDTO>> buscarPorAluno(@PathVariable Long id) {
		List<FeedbackForm> feedbacks = feedbackFormService.buscarFeedbackPorAluno(id);
		if (feedbacks != null) {
			List<FeedbackDTO> feedbackDtos = feedbacks.stream()
					.map(feedback -> new FeedbackDTO(feedback.getResposta1(), feedback.getResposta2(),
							feedback.getResposta3(), feedback.getResposta4(), feedback.getResposta5(),
							new CreatedByDTO(feedback.getCreatedBy().getNomeDocente(), feedback.getCreatedBy().getId()),
							new StudentDTO(feedback.getRecipientStudent().getNomeAluno(),
									feedback.getRecipientStudent().getId())))
					.toList();
			return new ResponseEntity<>(feedbackDtos, HttpStatus.OK);
		}
		return null;
	}

}
