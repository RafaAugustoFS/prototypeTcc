package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Calendario;

public interface CalendarioRepo extends JpaRepository<Calendario, Long>{
	
}
