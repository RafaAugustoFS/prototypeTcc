package com.onAcademy.tcc.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.onAcademy.tcc.config.TokenProvider;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.repository.TeacherRepo;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class TeacherService {

    public static final String ENROLLMENT_PREFIX = "p";

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;
    
    @Autowired
	private EmailService emailService;
	

    public String loginTeacher(String identifierCode, String password) {
        Teacher teacher = teacherRepo.findByidentifierCode(identifierCode)
                .filter(i -> passwordEncoder.matches(password, i.getPassword()))
                .orElseThrow(() -> new RuntimeException("Matricula ou senha incorretos"));
        return tokenProvider.generate(teacher.getId().toString(), List.of("teacher"));
    }
    @Transactional
    public Teacher criarTeacher(Teacher teacher) throws MessagingException {
        Teacher teacher1 = new Teacher();
        teacher1.setNomeDocente(teacher.getNomeDocente());
        teacher1.setDataNascimentoDocente(teacher.getDataNascimentoDocente());
        teacher1.setEmailDocente(teacher.getEmailDocente());
        teacher1.setTelefoneDocente(teacher.getTelefoneDocente());

        String year = String.valueOf(teacher.getDataNascimentoDocente().getYear());
        teacher1.setPassword(ENROLLMENT_PREFIX + year + teacher1.getNomeDocente().toLowerCase());
        
        
        String rawPassword = Teacher.generateRandomPassword(teacher);
	    String encodedPassword = passwordEncoder.encode(rawPassword);
	    teacher.setPassword(encodedPassword);
        
	    String encoded = passwordEncoder.encode(teacher1.getPassword());
        teacher1.setPassword(encoded);
	  
        Teacher saveTeacher = teacherRepo.save(teacher1);
       

        String emailSubject = "Bem-vindo ao OnAcademy!";
        String emailText = "<h1>Olá " + saveTeacher.getNomeDocente() + ",</h1>" +
                "<p>Seu cadastro foi realizado com sucesso!" + "<br/>" + 
        		"O código de matrícula é: " + saveTeacher.getIdentifierCode() + "<br/>" + 
                "Sua senha é: " + rawPassword + "</p>";
        emailService.sendEmail(saveTeacher.getEmailDocente(), emailSubject, emailText);
        
        return saveTeacher;
    }

    public List<Teacher> buscarTeachers() {
        return teacherRepo.findAll();
    }

    public Teacher atualizarTeacher(Long id, Teacher teacher) {
        Optional<Teacher> existingTeacher = teacherRepo.findById(id);
        if (existingTeacher.isPresent()) {
            Teacher atualizarTeacher = existingTeacher.get();
            atualizarTeacher.setNomeDocente(teacher.getNomeDocente());
            atualizarTeacher.setDataNascimentoDocente(teacher.getDataNascimentoDocente());
            atualizarTeacher.setEmailDocente(teacher.getEmailDocente());
            atualizarTeacher.setTelefoneDocente(teacher.getTelefoneDocente());
            String encodedPassword = passwordEncoder.encode(teacher.getPassword());
            atualizarTeacher.setPassword(encodedPassword);
            teacherRepo.save(atualizarTeacher);
            return atualizarTeacher;
        }
        return null;
    }

    public Teacher deletarTeacher(Long id) {
        Optional<Teacher> existingTeacher = teacherRepo.findById(id);
        if (existingTeacher.isPresent()) {
            Teacher deletarTeacher = existingTeacher.get();
            teacherRepo.delete(deletarTeacher);
            return deletarTeacher;
        }
        return null;
    }

    public Teacher buscarUnicoTeacher(Long id) {
        Optional<Teacher> existingTeacher = teacherRepo.findById(id);
        if (existingTeacher.isPresent()) {
            return existingTeacher.get();
        }
        return null;
    }


}
