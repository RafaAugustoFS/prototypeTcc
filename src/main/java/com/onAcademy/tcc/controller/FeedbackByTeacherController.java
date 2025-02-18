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

	record FeedbackDTO(String titulo, String conteudo, CreatedByDTO createdByDTO, StudentDTO student) {
	}

	record Feedback2DTO(String titulo, String conteudo, CreatedByDTO createdByDTO, ClassStDTO classSt) {
	}

	@PostMapping("/feedbackTeacher")
	public ResponseEntity<FeedbackByTeacher> criarFeedback(@RequestBody FeedbackByTeacher feedback) {
		FeedbackByTeacher feedback1 = feedbackByTeacherService.criarFeedback(feedback);
		return new ResponseEntity<>(feedback1, HttpStatus.OK);
	}

	@GetMapping("/feedbackTeacher")
	public ResponseEntity<List<FeedbackByTeacher>> buscarTodasFeedback() {
		List<FeedbackByTeacher> feedback = feedbackByTeacherService.buscarTodosFeedbacks();
		return new ResponseEntity<>(feedback, HttpStatus.OK);
	}

	@GetMapping("/feedbackTeacherStudent/{id}")
	public ResponseEntity<FeedbackDTO> buscarFeedbackStudentUnico(@PathVariable Long id) {
		FeedbackByTeacher buscaFeedback = feedbackByTeacherService.buscarFeedbackUnico(id);
		if (buscaFeedback != null) {
			var teacher = new CreatedByDTO(buscaFeedback.getCreatedBy().getNomeDocente(),
					buscaFeedback.getCreatedBy().getId());
			var student = new StudentDTO(buscaFeedback.getRecipientStudent().getNomeAluno(),
					buscaFeedback.getRecipientStudent().getId());
			var feedbackDTO = new FeedbackDTO(buscaFeedback.getTitulo(), buscaFeedback.getConteudo(), teacher, student);
			return new ResponseEntity<>(feedbackDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/feedbackTeacherClassSt/{id}")
	public ResponseEntity<Feedback2DTO> buscarFeedbackClassUnico(@PathVariable Long id) {
		FeedbackByTeacher buscaFeedback = feedbackByTeacherService.buscarFeedbackUnico(id);
		if (buscaFeedback != null) {
			var teacher = new CreatedByDTO(buscaFeedback.getCreatedBy().getNomeDocente(),
					buscaFeedback.getCreatedBy().getId());
			var classSt = new ClassStDTO(buscaFeedback.getClassSt().getNomeTurma(), buscaFeedback.getClassSt().getId());
			var feedback2DTO = new Feedback2DTO(buscaFeedback.getTitulo(), buscaFeedback.getConteudo(), teacher,
					classSt);
			return new ResponseEntity<>(feedback2DTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
