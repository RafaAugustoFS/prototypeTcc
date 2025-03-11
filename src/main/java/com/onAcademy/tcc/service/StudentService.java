package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.config.TokenProvider;
import com.onAcademy.tcc.dto.StudentClassDTO;
import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.repository.ClassStRepo;
import com.onAcademy.tcc.repository.StudentRepo;
import com.onAcademy.tcc.repository.TeacherRepo;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class StudentService {
	private static final String ENROLLMENT_PREFIX = "a";

	@Autowired
	private EmailService emailService;

	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private ClassStRepo classStRepo;

	@Autowired
	private TeacherRepo teacherRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;

	private String generateRandomNumber(int length) {
		String numbers = "0123456789";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			sb.append(numbers.charAt(random.nextInt(numbers.length())));
		}

		return sb.toString();
	}

	private String generateRandomPasswordWithName(int length, String nome) {
		String numbers = "0123456789";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			sb.append(numbers.charAt(random.nextInt(numbers.length())));
		}

		// Adiciona o nome do estudante ao final da senha
		String nomeFormatado = nome.replaceAll("\\s+", ""); // Remove espaços em branco do nome
		sb.append(nomeFormatado); // Adiciona o nome formatado à senha

		return sb.toString();
	}

	public String loginStudent(String identifierCode, String password) {
		Student student = studentRepo.findByidentifierCode(identifierCode)
				.filter(s -> passwordEncoder.matches(password, s.getPassword()))
				.orElseThrow(() -> new RuntimeException("Revise os campos!!"));
		return tokenProvider.generate(student.getId().toString(), List.of("student"));
	}

	@Transactional
	public Student criarEstudante(StudentClassDTO studentDTO) throws MessagingException {
		ClassSt classSt = classStRepo.findById(studentDTO.getTurmaId())
				.orElseThrow(() -> new RuntimeException("Turma não encontrada"));

		if (studentDTO.getNomeAluno().isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha com um nome.");
		}
		if (studentDTO.getDataNascimentoAluno() == null) {
			throw new IllegalArgumentException("Por favor preencha a data de nascimento.");
		}

		if (studentDTO.getEmailAluno().isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha o campo email.");
		}
		if (!studentDTO.getEmailAluno().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			throw new IllegalArgumentException("O email fornecido não tem formato válido.");
		}
		if (studentRepo.existsByEmailAluno(studentDTO.getEmailAluno())) {
			throw new IllegalArgumentException("Email já cadastrado.");

		} else if (teacherRepo.existsByEmailDocente(studentDTO.getEmailAluno())) {
			throw new IllegalArgumentException("Email já cadastrado.");
		}

		if (studentRepo.existsByTelefoneAluno(studentDTO.getTelefoneAluno())) {
			throw new IllegalArgumentException("Telefone já cadastrado.");
		}

		if (!studentDTO.getTelefoneAluno().matches("[0-9]+")) {
			throw new IllegalArgumentException("Telefone deve conter somente números.");
		}

		if (studentDTO.getTelefoneAluno().length() != 11) {
			throw new IllegalArgumentException("Telefone deve ter 11 dígitos.");
		}

		if (studentDTO.getTurmaId() == null) {
			throw new IllegalArgumentException("Por favor preencha o campo de turma.");
		}

		Student student = new Student();
		String year = String.valueOf(studentDTO.getDataNascimentoAluno().getYear());
		student.setNomeAluno(studentDTO.getNomeAluno());
		student.setDataNascimentoAluno(studentDTO.getDataNascimentoAluno());
		student.setEmailAluno(studentDTO.getEmailAluno());
		student.setTelefoneAluno(studentDTO.getTelefoneAluno());

		String rawPassword = Student.generateRandomPassword(studentDTO, classSt);
		String encodedPassword = passwordEncoder.encode(rawPassword);
		student.setPassword(encodedPassword);

		student.setTurmaId(classSt.getId());
		student.setPassword(encodedPassword);
		student.setImageUrl(studentDTO.getImageUrl());
		Student savedStudent = studentRepo.save(student);

		String emailSubject = "Bem-vindo ao OnAcademy!";
		String emailText = "<h1>Olá " + savedStudent.getNomeAluno() + ",</h1>"
				+ "<p>Seu cadastro foi realizado com sucesso!" + "<br/>" + "O código de matrícula é: "
				+ savedStudent.getIdentifierCode() + "<br/>" + "Sua senha é: " + rawPassword + "</p>";
		emailService.sendEmail(savedStudent.getEmailAluno(), emailSubject, emailText);

		return savedStudent;
	}

	public List<Student> buscarTodosEstudantes() {
		return studentRepo.findAll();
	}

	public Student atualizarEstudante(Long id, Student student) {
		Optional<Student> existStudentOpt = studentRepo.findById(id);
		if (existStudentOpt.isPresent()) {
			Student existStudent = existStudentOpt.get();

			existStudent.setNomeAluno(student.getNomeAluno());
			existStudent.setEmailAluno(student.getEmailAluno());
			existStudent.setDataNascimentoAluno(student.getDataNascimentoAluno());
			existStudent.setTelefoneAluno(student.getTelefoneAluno());
			existStudent.setImageUrl(student.getImageUrl());

			String identifierCode = ENROLLMENT_PREFIX + generateRandomNumber(6);
			existStudent.setIdentifierCode(identifierCode);

			String rawPassword = generateRandomPasswordWithName(6, existStudent.getNomeAluno()); 
			String encodedPassword = passwordEncoder.encode(rawPassword);
			existStudent.setPassword(encodedPassword);

			String emailSubject = "Seus dados de acesso foram atualizados!";
			String emailText = "<h1>Olá " + existStudent.getNomeAluno() + ",</h1>"
					+ "<p>Seus dados de acesso foram atualizados com sucesso!" + "<br/>"
					+ "Seu novo código de matrícula é: " + identifierCode + "<br/>" + "Sua nova senha é: " + rawPassword
					+ "</p>";
			try {
				emailService.sendEmail(existStudent.getEmailAluno(), emailSubject, emailText);
			} catch (MessagingException e) {
				throw new RuntimeException("Erro ao enviar email com os novos dados de acesso.", e);
			}

			return studentRepo.save(existStudent);
		}
		return null;
	}

	public Student buscarEstudanteUnico(Long id) {
		Optional<Student> existStudent = studentRepo.findById(id);
		return existStudent.orElse(null);
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
