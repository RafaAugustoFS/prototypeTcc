package com.onAcademy.tcc.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.repository.StudentRepo;

@Service
public class BoletimService {
	private final StudentRepo studentRepository;

    public BoletimService(StudentRepo studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Optional<Student> getStudentWithGrades(Long studentId) {
        return studentRepository.findById(studentId);
    }
}
