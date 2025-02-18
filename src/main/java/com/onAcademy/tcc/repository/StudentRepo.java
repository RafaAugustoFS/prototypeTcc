package com.onAcademy.tcc.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.onAcademy.tcc.model.Student;

public interface StudentRepo extends JpaRepository<Student, Long> {
	Optional<Student> findByidentifierCode(String registration);

}
