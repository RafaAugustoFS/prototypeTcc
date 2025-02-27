package com.onAcademy.tcc.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.onAcademy.tcc.config.TokenProvider;
import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.repository.TeacherRepo;

@Service
public class TeacherService {

    public static final String ENROLLMENT_PREFIX = "p";

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    public String loginTeacher(String identifierCode, String password) {
        Teacher teacher = teacherRepo.findByidentifierCode(identifierCode)
                .filter(i -> passwordEncoder.matches(password, i.getPassword()))
                .orElseThrow(() -> new RuntimeException("Matricula ou senha incorretos"));
        return tokenProvider.generate(teacher.getId().toString(), List.of("teacher"));
    }

    public Teacher criarTeacher(Teacher teacher) {
        Teacher teacher1 = new Teacher();
        teacher1.setNomeDocente(teacher.getNomeDocente());
        teacher1.setDataNascimentoDocente(teacher.getDataNascimentoDocente());
        teacher1.setEmailDocente(teacher.getEmailDocente());
        teacher1.setTelefoneDocente(teacher.getTelefoneDocente());

        String year = String.valueOf(teacher.getDataNascimentoDocente().getYear());
        teacher1.setPassword(ENROLLMENT_PREFIX + year + teacher1.getNomeDocente().toLowerCase());

        String encoded = passwordEncoder.encode(teacher1.getPassword());
        teacher1.setPassword(encoded);

        teacher1 = teacherRepo.save(teacher1);

        String identifierCode = generateIdentifierCode(teacher1.getId(), teacher1.getNomeDocente());
        teacher1.setIdentifierCode(identifierCode);

        return teacherRepo.save(teacher1);
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

    private String generateIdentifierCode(Long teacherId, String nomeDocente) {
        String year = String.valueOf(LocalDate.now().getYear());
        String formattedId = String.format("%04d", teacherId);
        String initials = nomeDocente.replaceAll("[^A-Za-z]", "")
                .substring(0, Math.min(2, nomeDocente.length()))
                .toUpperCase();
        return ENROLLMENT_PREFIX + year + formattedId + initials;
    }
}
