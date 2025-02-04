package com.onAcademy.tcc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.Calendario;
import com.onAcademy.tcc.repository.CalendarioRepo;

@Service
public class CalendarService {
	
	
	@Autowired
	private CalendarioRepo calendarioRepo;
	
	
	public Calendario criarCalendar(Calendario calendario) {
		Calendario criarCalendar = calendarioRepo.save(calendario);
		return criarCalendar;
	}

	public List<Calendario> buscarCalendarios() {
		List<Calendario> getCalendarios = calendarioRepo.findAll();
		return getCalendarios;
	}
}
