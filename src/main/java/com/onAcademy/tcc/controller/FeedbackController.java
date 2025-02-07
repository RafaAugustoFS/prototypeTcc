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
	
	@PostMapping("/feedback")
	public ResponseEntity<Feedback> criarFeedback(@RequestBody Feedback feedback){
		Feedback feedback1 = feedbackService.criarFeedback(feedback);
		return new ResponseEntity<>(feedback, HttpStatus.OK);
	}
	@GetMapping("/feedback")
	public ResponseEntity<List<Feedback>> buscarTodasFeedback(){
		List<Feedback> feedback = feedbackService.buscarTodosFeedbacks();
		return new ResponseEntity<>(feedback, HttpStatus.OK);
	}
	@GetMapping("/feedback/{id}")
	public ResponseEntity<Feedback> buscarClasseUnica(@PathVariable Long id, @RequestBody Feedback feedback){
		Feedback buscaFeedback = feedbackService.buscarFeedbackUnico(id);
		if(buscaFeedback != null) {
			return new ResponseEntity<>(buscaFeedback, HttpStatus.OK);
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
