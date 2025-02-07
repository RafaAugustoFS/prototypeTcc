package com.onAcademy.tcc.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.ClassSt;

public interface ClassStRepo extends JpaRepository<ClassSt, Long> {
	

}
