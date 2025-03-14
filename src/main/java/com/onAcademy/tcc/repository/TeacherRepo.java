package com.onAcademy.tcc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Teacher;

public interface TeacherRepo extends JpaRepository<Teacher, Long> {
	Optional<Teacher> findByidentifierCode(String identifierCode);
	boolean existsByEmailDocente(String emailDocente);
	boolean existsByTelefoneDocente(String telefoneDocente);
}
