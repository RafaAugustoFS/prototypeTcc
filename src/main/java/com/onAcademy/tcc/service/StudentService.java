package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.dto.StudentClassDTO;
import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.repository.ClassStRepo;
import com.onAcademy.tcc.repository.StudentRepo;

@Service
public class StudentService {
	@Autowired
	private StudentRepo studentRepo;
	
	@Autowired
	private ClassStRepo classStRepo;
	
	@Autowired
	private PasswordEncoder passworsEncoder;
	
	public Student criarEstudante(StudentClassDTO studentDTO) {
		
		ClassSt classSt = classStRepo.findById(studentDTO.getTurmaId())
			    .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
				
		String endodedPassword = passworsEncoder.encode(studentDTO.getSenhaAluno());
	
		studentDTO.setSenhaAluno(endodedPassword);
		
		Student student = new Student();
		
		student.setNomeAluno(studentDTO.getNomeAluno());
		student.setDataNascimentoAluno(studentDTO.getDataNascimentoAluno());
		student.setEmailAluno(studentDTO.getEmailAluno());
		student.setTelefoneAluno(studentDTO.getTelefoneAluno());
		student.setMatriculaAluno(studentDTO.getMatriculaAluno());
		student.setSenhaAluno(studentDTO.getSenhaAluno());
		
		student.setClassSt(classSt);
		
		return studentRepo.save(student);
	
		
	}
	public List<Student> buscarTodosEstudantes(){
		List<Student> buscarEstudantes = studentRepo.findAll();
		return buscarEstudantes;
	}
	public Student atualizarEstudante(Long id, Student student) {
		Optional<Student> existStudent = studentRepo.findById(id);
		if(existStudent.isPresent()) {
			Student atualizarEstudante = existStudent.get();
			atualizarEstudante.setNomeAluno(student.getNomeAluno());
			atualizarEstudante.setEmailAluno(student.getEmailAluno());
			atualizarEstudante.setDataNascimentoAluno(student.getDataNascimentoAluno());
			atualizarEstudante.setTelefoneAluno(student.getTelefoneAluno());
			atualizarEstudante.setSenhaAluno(student.getSenhaAluno());
			studentRepo.save(atualizarEstudante);
			return atualizarEstudante;
		}
		return null;
	}
	public Student buscarEstudanteUnico(Long id) {
		Optional<Student> existStudent = studentRepo.findById(id);
		if(existStudent.isPresent()) {
			return existStudent.get();
		}
		return null;
	}
	public Student deletarEstudante(Long id) {
		Optional<Student> existStudent = studentRepo.findById(id);
		if(existStudent.isPresent()) {
			Student deletarEstudante = existStudent.get();
			studentRepo.delete(deletarEstudante);
			return deletarEstudante;
		}
		return null;
	}
}
