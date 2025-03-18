package com.onAcademy.tcc.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.repository.StudentRepo;

/**
 * Serviço responsável por operações relacionadas ao boletim de estudantes.
 */
@Service
public class BoletimService {

    private final StudentRepo studentRepository;

    /**
     * Construtor para injetar a dependência do repositório de estudantes.
     *
     * @param studentRepository O repositório de estudantes.
     */
    public BoletimService(StudentRepo studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Busca um estudante pelo ID, incluindo suas notas.
     *
     * @param studentId O ID do estudante a ser buscado.
     * @return Um {@link Optional} contendo o estudante encontrado, ou vazio se não existir.
     */
    public Optional<Student> getStudentWithGrades(Long studentId) {
        return studentRepository.findById(studentId);
    }
}