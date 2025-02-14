package com.onAcademy.tcc.service; 

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.config.TokenProvider;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.repository.DisciplineRepo;
import com.onAcademy.tcc.repository.TeacherRepo;

@Service
public class TeacherService {
	
	@Autowired
	private TeacherRepo teacherRepo;
	
//	@Autowired
//	private DisciplineRepo disciplineRepo;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	public String loginTeacher(String identifierCode, String password) {
		Teacher teacher = teacherRepo.findByidentifierCode(identifierCode)
				.filter(i -> passwordEncoder.matches(password, i.getPassword()))
				.orElseThrow(()-> new RuntimeException("Matricula ou senha incorretos"));
		return tokenProvider.generate(teacher.getId().toString(),List.of("teacher"));
	}
	
	public Teacher criarTeacher(Teacher teacher) {
		
//		Discipline discipline = disciplineRepo.findById(teacher.getDisciplinaId())
//			    .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
		
		String encodedPassword = passwordEncoder.encode(teacher.getPassword());
		
		teacher.setPassword(encodedPassword);
		
//		teacher.setDisciplinaId(discipline.getId());
		
		
		Teacher salvarTeacher = teacherRepo.save(teacher);
		return salvarTeacher;
	}
	
	
	public List<Teacher> buscarTeachers(){
		List<Teacher> teacher = teacherRepo.findAll();
		return teacher;
	}
	
	public Teacher atualizarTeacher(Long id, Teacher teacher) {
		Optional<Teacher> existingTeacher = teacherRepo.findById(id);
		if(existingTeacher.isPresent()) {
			Teacher atualizarTeacher = existingTeacher.get();
			atualizarTeacher.setNomeDocente(teacher.getNomeDocente());
			atualizarTeacher.setDataNascimentoDocente(teacher.getDataNascimentoDocente());
			//atualizarTeacher.setClassTeachers(teacher.getDisciplineTeachers());
			atualizarTeacher.setEmailDocente(teacher.getEmailDocente());
			atualizarTeacher.setTelefoneDocente(teacher.getTelefoneDocente());
			atualizarTeacher.setPassword(teacher.getPassword());
			teacherRepo.save(atualizarTeacher);
			return atualizarTeacher;
		}
		
		return null;
	}
	
	public Teacher deletarTeacher(Long id) {
		Optional<Teacher> existingTeacher = teacherRepo.findById(id);
		if(existingTeacher.isPresent()) {
			Teacher deletarTeacher = existingTeacher.get();
			teacherRepo.deleteById(id);
			teacherRepo.save(deletarTeacher);
			return deletarTeacher;
			
		}
		
		
		return null;
	}
	
	public Teacher buscarUnicoTeacher(Long id) {
		Optional<Teacher> existingTeacher = teacherRepo.findById(id);
		if(existingTeacher.isPresent()) {
			Teacher buscarTeacher = existingTeacher.get();
			return buscarTeacher;
		}
		return null;
	}

	
	
}
