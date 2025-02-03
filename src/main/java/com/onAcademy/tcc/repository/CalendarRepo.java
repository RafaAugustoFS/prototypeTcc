package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Calendar;

public interface CalendarRepo extends JpaRepository<Calendar, Long>{
	
}
