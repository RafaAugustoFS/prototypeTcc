package com.onAcademy.tcc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onAcademy.tcc.model.Calendario;
import com.onAcademy.tcc.service.CalendarService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Calendar", description = "EndPoint de calendário")
@RestController
@RequestMapping("/api")
public class CalendarioController {
	
	@Autowired
	private CalendarService calendarService;
	
	@PostMapping("/calendario")
	public ResponseEntity<Calendario> criarCalendario(@RequestBody Calendario calendario){
		Calendario criarCalendario = calendarService.criarCalendar(calendario);
		return new ResponseEntity<>(criarCalendario, HttpStatus.CREATED);
	}
	
	@GetMapping("/calendario")
	public ResponseEntity<List<Calendario>> buscarCalendario () {
		List<Calendario> getCalendario = calendarService.buscarCalendarios();
		return new ResponseEntity<>(getCalendario, HttpStatus.OK);
	}

}
