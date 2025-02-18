package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Discipline;

public interface DisciplineRepo extends JpaRepository<Discipline, Long> {

}
