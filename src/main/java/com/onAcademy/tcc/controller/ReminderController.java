package com.onAcademy.tcc.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onAcademy.tcc.model.Reminder;
import com.onAcademy.tcc.service.ReminderService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Lembrete", description = "EndPoint de lembrete")
@RestController
@RequestMapping("/api")
public class ReminderController {
	record CreatedByDTO(String nomeDocente, Long id, String getInitials) {
	}

	record CreatedByInstitutionDTO(String nameInstitution, Long id, String getInitials) {
	}

	record ReminderDTO(Long id, String conteudo, LocalDateTime horarioSistema, String criadoPorNome, Long criadoPorId, String initials, Long classStId) {
	}

	@Autowired
	private ReminderService reminderService;

	@PostMapping("/reminder")
	public ResponseEntity<Reminder> criarLembrete(@RequestBody Reminder reminder) {
		Reminder reminder1 = reminderService.criarLembrete(reminder);
		return new ResponseEntity<>(reminder1, HttpStatus.OK);
	}

	@GetMapping("/reminder")
	public ResponseEntity<List<ReminderDTO>> buscarTodosLembretes() {
	    List<Reminder> reminders = reminderService.buscarTodosLembretes();
	    
	    if (reminders != null && !reminders.isEmpty()) {
	        List<ReminderDTO> reminderDTOS = reminders.stream().map(reminder -> 
	            new ReminderDTO(
	                reminder.getId(),
	                reminder.getConteudo(),
	                reminder.getHorarioSistema(),
	                reminder.getCreatedBy() != null ? reminder.getCreatedBy().getNomeDocente() : 
	                (reminder.getCreatedByInstitution() != null ? reminder.getCreatedByInstitution().getNameInstitution() : "Desconhecido"),
	                reminder.getCreatedBy() != null ? reminder.getCreatedBy().getId() : 
	                (reminder.getCreatedByInstitution() != null ? reminder.getCreatedByInstitution().getId() : null),
	                reminder.getInitials(),
	                reminder.getClassSt() != null ? reminder.getClassSt().getId() : null
	            )
	        ).toList();
	        
	        return new ResponseEntity<>(reminderDTOS, HttpStatus.OK);
	    }
	    
	    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/reminder/{classStId}")
	public ResponseEntity<List<ReminderDTO>> buscarPorClassStId(@PathVariable Long classStId) {
	    List<Reminder> lembretes = reminderService.buscarLembretePorClassStId(classStId);

	    if (lembretes.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    List<ReminderDTO> reminderDTOs = lembretes.stream().map(lembrete -> 
	        new ReminderDTO(
	            lembrete.getId(),
	            lembrete.getConteudo(),
	            lembrete.getHorarioSistema(),
	            lembrete.getCreatedBy() != null ? lembrete.getCreatedBy().getNomeDocente() : 
	            (lembrete.getCreatedByInstitution() != null ? lembrete.getCreatedByInstitution().getNameInstitution() : "Desconhecido"),
	            lembrete.getCreatedBy() != null ? lembrete.getCreatedBy().getId() : 
	            (lembrete.getCreatedByInstitution() != null ? lembrete.getCreatedByInstitution().getId() : null),
	            lembrete.getInitials(),
	            lembrete.getClassSt() != null ? lembrete.getClassSt().getId() : null
	        )
	    ).collect(Collectors.toList());

	    return new ResponseEntity<>(reminderDTOs, HttpStatus.OK);
	}

	@GetMapping("/reminder/teacher/{teacherId}")
	public ResponseEntity<List<ReminderDTO>> buscarPorCreatedBy(@PathVariable Long teacherId) {
	    List<Reminder> lembretes = reminderService.buscarLembretePorCreatedBy(teacherId);

	    if (lembretes.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    List<ReminderDTO> reminderDTOs = lembretes.stream().map(lembrete -> 
	        new ReminderDTO(
	            lembrete.getId(),
	            lembrete.getConteudo(),
	            lembrete.getHorarioSistema(),
	            lembrete.getCreatedBy() != null ? lembrete.getCreatedBy().getNomeDocente() : 
	            (lembrete.getCreatedByInstitution() != null ? lembrete.getCreatedByInstitution().getNameInstitution() : "Desconhecido"),
	            lembrete.getCreatedBy() != null ? lembrete.getCreatedBy().getId() : 
	            (lembrete.getCreatedByInstitution() != null ? lembrete.getCreatedByInstitution().getId() : null),
	            lembrete.getInitials(),
	            lembrete.getClassSt() != null ? lembrete.getClassSt().getId() : null
	        )
	    ).collect(Collectors.toList());

	    return new ResponseEntity<>(reminderDTOs, HttpStatus.OK);
	}



}
