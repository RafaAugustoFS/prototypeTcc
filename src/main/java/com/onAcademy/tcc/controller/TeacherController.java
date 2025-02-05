package com.onAcademy.tcc.controller;

import java.util.List;
import java.util.Optional;

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

import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.service.TeacherService;

@RestController
@RequestMapping("/api")
public class TeacherController {
	
	@Autowired
	private TeacherService teacherService;
	
	@PostMapping("/teacher")
	public ResponseEntity<Teacher> criarTeacher(@RequestBody Teacher teacher){
		Teacher criarTeacher = teacherService.criarTeacher(teacher);
		return new ResponseEntity<>(criarTeacher, HttpStatus.CREATED);
	}
	
	@GetMapping("/teacher")
	public ResponseEntity<List<Teacher>> buscarTeachers(){
		List<Teacher> teachers = teacherService.buscarTeachers();
		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}
	
	@PutMapping("/teacher/{id}")
	public ResponseEntity<Teacher> editarTeacher(@PathVariable Long id, @RequestBody Teacher teacher ){
		Teacher atualizarTeachers = teacherService.atualizarTeacher(id, teacher);
		
		if(atualizarTeachers != null) {
			return new ResponseEntity<>(atualizarTeachers, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/teacher/{id}")
	public ResponseEntity<Teacher> buscarTeacherUnico (@PathVariable Long id){
		Teacher buscarUnico = teacherService.buscarUnicoTeacher(id);
		return new ResponseEntity<>(buscarUnico, HttpStatus.OK);
	}
	
	@DeleteMapping("/teacher/{id}")
	public ResponseEntity<Teacher> deletarTeacher(@PathVariable Long id){
		Teacher deletarTeacher = teacherService.deletarTeacher(id);
		return new ResponseEntity<>(deletarTeacher, HttpStatus.OK);
	}
	

}
