package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.onAcademy.tcc.model.Student;

public interface StudentRepo extends JpaRepository<Student, Long>{

	 
}
