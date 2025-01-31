package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Institution;

public interface InstitutionRepo extends JpaRepository<Institution, Long> {

}
