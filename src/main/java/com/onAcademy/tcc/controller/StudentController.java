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

import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.service.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Student", description = "EndPoint de estudante")
@RestController
@RequestMapping("/api")
public class StudentController {
	@Autowired
	private StudentService studentService;
	
	@PostMapping("/student")
	public ResponseEntity<Student> criarEstudante(@RequestBody Student student){
		Student student1 = studentService.criarEstudante(student);
		return new ResponseEntity<>(student1, HttpStatus.CREATED);
	}
	@GetMapping("/student")
	public ResponseEntity<List<Student>> buscarTodosEstudantes(){
		List<Student> student = studentService.buscarTodosEstudantes();
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
	@GetMapping("/student/{id}")
	public ResponseEntity<Student> buscarEstudanteUnico(@PathVariable Long id){
		Student buscaEstudante = studentService.buscarEstudanteUnico(id);
		if(buscaEstudante != null) {
			return new ResponseEntity<>(buscaEstudante, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	@PutMapping("/student/{id}")
	public ResponseEntity<Student> atualizarEstudante(@PathVariable Long id,@RequestBody Student student){
		Student atualizarEstudante = studentService.atualizarEstudante(id, student);
		if(atualizarEstudante != null) {
			return new ResponseEntity<>(atualizarEstudante, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	@DeleteMapping("/student/{id}")
	public ResponseEntity<Student> deletarEstudante(@PathVariable Long id){
		Student deletarEstudante = studentService.deletarEstudante(id);
		if(deletarEstudante != null) {
			return new ResponseEntity<>(deletarEstudante, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	} 
}
