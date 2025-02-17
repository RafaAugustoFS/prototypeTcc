package com.onAcademy.tcc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onAcademy.tcc.dto.LoginTeacherDTO;
import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.repository.ClassStRepo;
import com.onAcademy.tcc.repository.TeacherRepo;
import com.onAcademy.tcc.service.TeacherService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Teacher", description = "EndPoint de professor")
@RestController
@RequestMapping("/api")
public class TeacherController {
	record ClassTeacherDTO(String nomeTurma, Long turmaId ){}
	record TeacherDTO(Long turmaId, String nomeDocente, List<ClassTeacherDTO> ClassTeacherDTO){}
	
	
	@Autowired
	private ClassStRepo classStRepo;
	
	
	@Autowired
	private  TeacherRepo teacherRepo;
	
	@Autowired
	private TeacherService teacherService;
	
	@PostMapping("/teacher/login")
    public ResponseEntity<Map<String,String>> loginTeacher(@RequestBody LoginTeacherDTO loginTeacherDTO) {
        String token = teacherService.loginTeacher(loginTeacherDTO.identifierCode(), loginTeacherDTO.password());
        
        Map<String,String> response = new HashMap<>();
        response.put("token", token);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	
	@PostMapping("/teacher")	
	@PreAuthorize("hasRole('INSTITUTION')")
	public ResponseEntity<Teacher> criarTeacher(@RequestBody TeacherDTO teacherDTO){
		ClassSt classSt = classStRepo.findById(teacherDTO.turmaId())
				.orElseThrow(() -> new RuntimeException("Turma não encontrada"));
		Teacher teacher = new Teacher();
		teacher.setNomeDocente(teacherDTO.nomeDocente());
		
		
		if (teacher.getClasses() == null) {
		    teacher.setClasses(new ArrayList<>()); // Inicializa a lista
		}
		teacher.getClasses().add(classSt);

		teacherRepo.save(teacher);

		
		return new ResponseEntity<>(teacher, HttpStatus.CREATED);
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
