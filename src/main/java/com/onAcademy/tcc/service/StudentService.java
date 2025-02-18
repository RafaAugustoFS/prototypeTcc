package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.onAcademy.tcc.config.TokenProvider;
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

	@Autowired
	private TokenProvider tokenProvider;

	public String loginStudent(String identifierCode, String password) {
		Student student = studentRepo.findByidentifierCode(identifierCode)
				.filter(s -> passworsEncoder.matches(password, s.getPassword()))
				.orElseThrow(() -> new RuntimeException("Revise os campos!!"));
		return tokenProvider.generate(student.getId().toString(), List.of("student"));

	}

	public Student criarEstudante(StudentClassDTO studentDTO) {

		ClassSt classSt = classStRepo.findById(studentDTO.getTurmaId())
				.orElseThrow(() -> new RuntimeException("Turma não encontrada"));

		String endodedPassword = passworsEncoder.encode(studentDTO.getPassword());

		Student student = new Student();

		student.setNomeAluno(studentDTO.getNomeAluno());
		student.setDataNascimentoAluno(studentDTO.getDataNascimentoAluno());
		student.setEmailAluno(studentDTO.getEmailAluno());
		student.setTelefoneAluno(studentDTO.getTelefoneAluno());
		student.setIdentifierCode(studentDTO.getIdentifierCode());
		student.setPassword(endodedPassword);

		student.setTurmaId(classSt.getId());
		return studentRepo.save(student);

	}

	public List<Student> buscarTodosEstudantes() {
		List<Student> buscarEstudantes = studentRepo.findAll();
		return buscarEstudantes;
	}

	public Student atualizarEstudante(Long id, Student student) {
		Optional<Student> existStudent = studentRepo.findById(id);
		if (existStudent.isPresent()) {
			Student atualizarEstudante = existStudent.get();
			atualizarEstudante.setNomeAluno(student.getNomeAluno());
			atualizarEstudante.setEmailAluno(student.getEmailAluno());
			atualizarEstudante.setDataNascimentoAluno(student.getDataNascimentoAluno());
			atualizarEstudante.setTelefoneAluno(student.getTelefoneAluno());
			atualizarEstudante.setPassword(student.getPassword());
			studentRepo.save(atualizarEstudante);
			return atualizarEstudante;
		}
		return null;
	}

	public Student buscarEstudanteUnico(Long id) {
		Optional<Student> existStudent = studentRepo.findById(id);
		if (existStudent.isPresent()) {
			return existStudent.get();
		}
		return null;
	}

	public Student deletarEstudante(Long id) {
		Optional<Student> existStudent = studentRepo.findById(id);
		if (existStudent.isPresent()) {
			Student deletarEstudante = existStudent.get();
			studentRepo.delete(deletarEstudante);
			return deletarEstudante;
		}
		return null;
	}
}
