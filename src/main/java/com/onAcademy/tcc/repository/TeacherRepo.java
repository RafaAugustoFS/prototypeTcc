package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Teacher;

public interface TeacherRepo extends JpaRepository<Teacher, Long>{

}
