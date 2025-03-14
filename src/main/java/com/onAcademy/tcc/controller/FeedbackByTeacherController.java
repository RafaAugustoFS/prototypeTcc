package com.onAcademy.tcc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onAcademy.tcc.model.FeedbackByTeacher;
import com.onAcademy.tcc.service.FeedbackByTeacherService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Feedback teacher", description = "EndPoint de feedback do professor")
@RestController
@RequestMapping("/api")
public class FeedbackByTeacherController {

	@Autowired
	private FeedbackByTeacherService feedbackByTeacherService;

	record StudentDTO(String nomeAluno, Long id) {
	}

	record ClassStDTO(String nomeTurma, Long id) {
	}

	record CreatedByDTO(String nomeDocente, Long id) {
	}

	record FeedbackDTO(String conteudo, CreatedByDTO createdByDTO, StudentDTO student) {
	}

	record Feedback2DTO(String conteudo, CreatedByDTO createdByDTO, ClassStDTO classSt) {
	}

	@PostMapping("/feedbackTeacher")
	public ResponseEntity<?> criarFeedback(@RequestBody FeedbackByTeacher feedback) {
		try {
			validarFeedbackTeacher(feedback);
			FeedbackByTeacher feedback1 = feedbackByTeacherService.criarFeedback(feedback);
			return new ResponseEntity<>(feedback1, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao criar feedback: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	public void validarFeedbackTeacher(FeedbackByTeacher feedback) {
		if (feedback.getConteudo().isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha todos os campos.");
		}

	}

	@GetMapping("/feedbackTeacher")
	public ResponseEntity<?> buscarTodasFeedback() {
		try {
			List<FeedbackByTeacher> feedbacks = feedbackByTeacherService.buscarTodosFeedbacks();
			return new ResponseEntity<>(feedbacks, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar feedbacks: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/feedbackTeacherStudent/{id}")
	public ResponseEntity<?> buscarFeedbackStudentUnico(@PathVariable Long id) {
		try {
			FeedbackByTeacher buscaFeedback = feedbackByTeacherService.buscarFeedbackUnico(id);
			if (buscaFeedback != null) {
				var teacher = new CreatedByDTO(buscaFeedback.getCreatedBy().getNomeDocente(),
						buscaFeedback.getCreatedBy().getId());
				var student = new StudentDTO(buscaFeedback.getRecipientStudent().getNomeAluno(),
						buscaFeedback.getRecipientStudent().getId());
				var feedbackDTO = new FeedbackDTO(buscaFeedback.getConteudo(), teacher,
						student);
				return new ResponseEntity<>(feedbackDTO, HttpStatus.OK);
			}
			return new ResponseEntity<>(Map.of("error", "Feedback não encontrado"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar feedback: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/feedbackTeacherClassSt/{id}")
	public ResponseEntity<?> buscarFeedbackClassUnico(@PathVariable Long id) {
		try {
			FeedbackByTeacher buscaFeedback = feedbackByTeacherService.buscarFeedbackUnico(id);
			if (buscaFeedback != null) {
				var teacher = new CreatedByDTO(buscaFeedback.getCreatedBy().getNomeDocente(),
						buscaFeedback.getCreatedBy().getId());
				var classSt = new ClassStDTO(buscaFeedback.getClassSt().getNomeTurma(),
						buscaFeedback.getClassSt().getId());
				var feedback2DTO = new Feedback2DTO(buscaFeedback.getConteudo(), teacher,
						classSt);
				return new ResponseEntity<>(feedback2DTO, HttpStatus.OK);
			}
			return new ResponseEntity<>(Map.of("error", "Feedback não encontrado"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar feedback: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}
}
