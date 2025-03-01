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
	record ReminderDTO(Long id, String conteudo, LocalDateTime horarioSistema, CreatedByDTO createdBy, Long classStId) {
    }
	@Autowired
	private ReminderService reminderService;
	
	@PostMapping("/reminder")
	public ResponseEntity<Reminder> criarLembrete(@RequestBody Reminder reminder) {
		Reminder reminder1 = reminderService.criarLembrete(reminder	);
		return new ResponseEntity<>(reminder1, HttpStatus.OK);
	}

	@GetMapping("/reminder")
	public ResponseEntity<List<ReminderDTO>> buscarTodosLembretes() {
		List<Reminder> reminder = reminderService.buscarTodosLembretes();
		if(reminder != null) {
			List<ReminderDTO> reminderDTos = reminder.stream().map(reminders ->
			new ReminderDTO(
				reminders.getId(),
				reminders.getConteudo(),
				reminders.getHorarioSistema(),
				new CreatedByDTO(
				reminders.getCreatedBy().getNomeDocente(), reminders.getCreatedBy().getId(), reminders.getCreatedBy().getInitials()
					),
				null
				)
			).toList();
			return new ResponseEntity<>(reminderDTos, HttpStatus.OK);
		}
		 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/reminder/{classStId}")
	public ResponseEntity<List<ReminderDTO>> buscarPorClassStId(@PathVariable Long classStId) {
        List<Reminder> lembretes = reminderService.buscarLembretePorClassStId(classStId);
        
        if (lembretes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convertendo para DTO
        List<ReminderDTO> reminderDTOs = lembretes.stream().map(lembrete -> {
            CreatedByDTO createdByDTO = new CreatedByDTO(
                    lembrete.getCreatedBy() != null ? lembrete.getCreatedBy().getNomeDocente() : null,
                    lembrete.getCreatedBy() != null ? lembrete.getCreatedBy().getId() : null,
                    lembrete.getCreatedBy() != null ? lembrete.getCreatedBy().getInitials() : null
            );
            return new ReminderDTO(lembrete.getId(), lembrete.getConteudo(), lembrete.getHorarioSistema(), createdByDTO, lembrete.getClassSt().getId());
        }).collect(Collectors.toList());

        return new ResponseEntity<>(reminderDTOs, HttpStatus.OK);  // Retorna a lista de lembretes
    }
	
}
