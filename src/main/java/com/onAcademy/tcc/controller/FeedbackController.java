package com.onAcademy.tcc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onAcademy.tcc.model.Feedback;
import com.onAcademy.tcc.service.FeedbackService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Feedback", description = "EndPoint de feedback")
@RestController
@RequestMapping("/api")
public class FeedbackController {
	@Autowired
	private FeedbackService feedbackService;
	
	record StudentDTO(String nomeAluno, Long id) {}
	
	record TeacherDTO(String nomeTeacher, Long id) {}
	
	record ClassStDTO(String nomeTurma, Long id) {}
	
	record FeedbackDTO(String titulo, String conteudo, StudentDTO student) {}
	
	record Feedback2DTO(String titulo, String conteudo, TeacherDTO teacher) {}
	
	record Feedback3DTO(String titulo, String conteudo, ClassStDTO classSt) {}
	
	@PostMapping("/feedback")
	public ResponseEntity<Feedback> criarFeedback(@RequestBody Feedback feedback){
		Feedback feedback1 = feedbackService.criarFeedback(feedback);
		return new ResponseEntity<>(feedback1, HttpStatus.OK);
	}
	@GetMapping("/feedback")
	public ResponseEntity<List<Feedback>> buscarTodasFeedback(){
		List<Feedback> feedback = feedbackService.buscarTodosFeedbacks();
		return new ResponseEntity<>(feedback, HttpStatus.OK);
	}
	@GetMapping("/feedbackStudent/{id}")
	public ResponseEntity<FeedbackDTO> buscarFeedbackStudentUnico(@PathVariable Long id){
		Feedback buscaFeedback = feedbackService.buscarFeedbackUnico(id);
		if(buscaFeedback != null) {
			var student = new StudentDTO(buscaFeedback.getRecipientStudent().getNomeAluno(), buscaFeedback.getRecipientStudent().getId() );
			var feedbackDTO = new FeedbackDTO(buscaFeedback.getTitulo(), buscaFeedback.getConteudo(), student);
			return new ResponseEntity<>(feedbackDTO ,HttpStatus.OK);
			
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	@GetMapping("/feedbackTeacher/{id}")
	public ResponseEntity<Feedback2DTO> buscarFeedbackTeacherUnico(@PathVariable Long id){
		Feedback buscaFeedback = feedbackService.buscarFeedbackUnico(id);
		if(buscaFeedback != null) {
			var teacher = new TeacherDTO(buscaFeedback.getRecipientTeacher().getNomeDocente(), buscaFeedback.getRecipientTeacher().getId());
			var feedback2DTO = new Feedback2DTO(buscaFeedback.getTitulo(), buscaFeedback.getConteudo(), teacher);
			return new ResponseEntity<>(feedback2DTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	@GetMapping("/feedbackClass/{id}")
	public ResponseEntity<Feedback3DTO> buscarFeedbackClassUnico(@PathVariable Long id){
		Feedback buscaFeedback = feedbackService.buscarFeedbackUnico(id);
		if(buscaFeedback != null) {
			var classSt = new ClassStDTO(buscaFeedback.getClassSt().getNomeTurma(), buscaFeedback.getClassSt().getId() );
			var feedback3DTO = new Feedback3DTO(buscaFeedback.getTitulo(), buscaFeedback.getConteudo(), classSt);
			return new ResponseEntity<>(feedback3DTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	@PutMapping("/feedback/{id}")
	public ResponseEntity<Feedback> atualizarFeedback(@PathVariable Long id, @RequestBody Feedback feedback){
		Feedback atualizarFeedback = feedbackService.atualizarFeedback(id, feedback);
		if(atualizarFeedback != null) {
			return new ResponseEntity<>(atualizarFeedback, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	@DeleteMapping("/feedback/{id}")
	public ResponseEntity<Feedback> deletarFeedback(@PathVariable Long id){
		Feedback deletarFeedback = feedbackService.deletarFeedback(id);
		if(deletarFeedback != null) {
			return new ResponseEntity<>(deletarFeedback, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
