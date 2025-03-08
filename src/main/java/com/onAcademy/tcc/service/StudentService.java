package com.onAcademy.tcc.service;

import java.time.LocalDate;
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
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;

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

        if(studentDTO.getEmailAluno().isEmpty()) {
        	throw new IllegalArgumentException("Por favor preencha o campo email.");
        } 
        if(!studentDTO.getEmailAluno().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
          	 throw new IllegalArgumentException("O email fornecido não tem formato válido.");
          }
        if (studentRepo.existsByEmailAluno(studentDTO.getEmailAluno())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        } else if (studentRepo.existsByTelefoneAluno(studentDTO.getTelefoneAluno())) {
            throw new IllegalArgumentException("Telefone já cadastrado.");
        }

        if (!studentDTO.getTelefoneAluno().matches("[0-9]+")) {
            throw new IllegalArgumentException("Telefone deve conter somente números.");
        }

        if (studentDTO.getTelefoneAluno().length() != 11) {
            throw new IllegalArgumentException("Telefone deve ter 11 dígitos.");
        }
        
        if(studentDTO.getTurmaId() == null) {
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

        Student savedStudent = studentRepo.save(student);

        String emailSubject = "Bem-vindo ao OnAcademy!";
        String emailText = "<h1>Olá " + savedStudent.getNomeAluno() + ",</h1>" +
                "<p>Seu cadastro foi realizado com sucesso!" + "<br/>" + 
        		"O código de matrícula é: " + savedStudent.getIdentifierCode() + "<br/>" + 
                "Sua senha é: " + rawPassword + "</p>";
        emailService.sendEmail(savedStudent.getEmailAluno(), emailSubject, emailText);

        return savedStudent;
    }

	public List<Student> buscarTodosEstudantes() {
		return studentRepo.findAll();
	}

	public Student atualizarEstudante(Long id, Student student) {
		Optional<Student> existStudent = studentRepo.findById(id);
		if (existStudent.isPresent()) {
			Student atualizarEstudante = existStudent.get();
			atualizarEstudante.setNomeAluno(student.getNomeAluno());
			atualizarEstudante.setEmailAluno(student.getEmailAluno());
			atualizarEstudante.setDataNascimentoAluno(student.getDataNascimentoAluno());
			atualizarEstudante.setTelefoneAluno(student.getTelefoneAluno());
			String encoderPassword = passwordEncoder.encode(student.getPassword());
			atualizarEstudante.setPassword(encoderPassword);
			studentRepo.save(atualizarEstudante);
			return atualizarEstudante;
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
