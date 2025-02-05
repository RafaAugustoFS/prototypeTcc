package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.repository.StudentRepo;

@Service
public class StudentService {
	@Autowired
	private StudentRepo studentRepo;
	
	@Autowired
	private PasswordEncoder passworsEncoder;
	
	public Student criarEstudante(Student student) {
		String endodedPassword = passworsEncoder.encode(student.getSenhaAluno());
		
		student.setSenhaAluno(endodedPassword);
		
		Student salvarEstudante = studentRepo.save(student);
		return salvarEstudante;
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
