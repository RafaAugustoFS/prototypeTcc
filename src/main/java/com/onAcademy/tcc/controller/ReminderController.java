package com.onAcademy.tcc.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

	record ReminderDTO(Long id, String conteudo, LocalDateTime horarioSistema, String criadoPorNome, Long criadoPorId,
			String initials, Long classStId) {
	}

	@Autowired
	private ReminderService reminderService;

	/**
	 * Cria um novo lembrete.
	 * 
	 * - Recebe os dados do lembrete via requisição HTTP POST. - Se o conteúdo do
	 * lembrete for vazio, lança uma exceção. - Cria o lembrete no sistema e retorna
	 * o lembrete criado.
	 * 
	 * @param reminder Objeto contendo os dados do lembrete a ser criado.
	 * @return Resposta HTTP com o lembrete criado.
	 */
	@PreAuthorize("hasAnyRole('INSTITUTION','TEACHER')")
	@PostMapping("/reminder")
	public ResponseEntity<Reminder> criarLembrete(@RequestBody Reminder reminder) {
		if (reminder.getConteudo().isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha todos os campos.");
		}
		Reminder reminder1 = reminderService.criarLembrete(reminder);
		return new ResponseEntity<>(reminder1, HttpStatus.OK);
	}

	/**
	 * Recupera todos os lembretes registrados no sistema.
	 * 
	 * - Retorna todos os lembretes, com informações detalhadas sobre quem os criou
	 * e a turma associada. - Se não houver lembretes, retorna uma resposta HTTP 404
	 * (Not Found).
	 * 
	 * @return Resposta HTTP com a lista de todos os lembretes ou erro (404).
	 */
	@GetMapping("/reminder")
	public ResponseEntity<List<ReminderDTO>> buscarTodosLembretes() {
		List<Reminder> reminders = reminderService.buscarTodosLembretes();

		if (reminders != null && !reminders.isEmpty()) {
			List<ReminderDTO> reminderDTOS = reminders.stream()
					.map(reminder -> new ReminderDTO(reminder.getId(), reminder.getConteudo(),
							reminder.getHorarioSistema(),
							reminder.getCreatedBy() != null ? reminder.getCreatedBy().getNomeDocente()
									: (reminder.getCreatedByInstitution() != null
											? reminder.getCreatedByInstitution().getNameInstitution()
											: "Desconhecido"),
							reminder.getCreatedBy() != null ? reminder.getCreatedBy().getId()
									: (reminder.getCreatedByInstitution() != null
											? reminder.getCreatedByInstitution().getId()
											: null),
							reminder.getInitials(),
							reminder.getClassSt() != null ? reminder.getClassSt().getId() : null))
					.toList();

			return new ResponseEntity<>(reminderDTOS, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Busca lembretes associados a uma turma específica.
	 * 
	 * - Recebe o ID da turma como parâmetro e retorna todos os lembretes
	 * relacionados a essa turma. - Caso não existam lembretes para a turma, retorna
	 * uma resposta HTTP 404 (Not Found).
	 * 
	 * @param classStId ID da turma.
	 * @return Resposta HTTP com a lista de lembretes para a turma ou erro (404).
	 */
	@GetMapping("/reminder/{classStId}")
	public ResponseEntity<List<ReminderDTO>> buscarPorClassStId(@PathVariable Long classStId) {
		List<Reminder> lembretes = reminderService.buscarLembretePorClassStId(classStId);

		if (lembretes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<ReminderDTO> reminderDTOs = lembretes.stream()
				.map(lembrete -> new ReminderDTO(lembrete.getId(), lembrete.getConteudo(), lembrete.getHorarioSistema(),
						lembrete.getCreatedBy() != null ? lembrete.getCreatedBy().getNomeDocente()
								: (lembrete.getCreatedByInstitution() != null
										? lembrete.getCreatedByInstitution().getNameInstitution()
										: "Desconhecido"),
						lembrete.getCreatedBy() != null ? lembrete.getCreatedBy().getId()
								: (lembrete.getCreatedByInstitution() != null
										? lembrete.getCreatedByInstitution().getId()
										: null),
						lembrete.getInitials(), lembrete.getClassSt() != null ? lembrete.getClassSt().getId() : null))
				.collect(Collectors.toList());

		return new ResponseEntity<>(reminderDTOs, HttpStatus.OK);
	}

	/**
	 * Busca lembretes criados por um docente específico.
	 * 
	 * - Recebe o ID do docente como parâmetro e retorna todos os lembretes criados
	 * por ele. - Se não houver lembretes, retorna uma resposta HTTP 404 (Not
	 * Found).
	 * 
	 * @param teacherId ID do docente.
	 * @return Resposta HTTP com a lista de lembretes criados pelo docente ou erro
	 *         (404).
	 */
	@GetMapping("/reminder/teacher/{teacherId}")
	public ResponseEntity<List<ReminderDTO>> buscarPorCreatedBy(@PathVariable Long teacherId) {
		List<Reminder> lembretes = reminderService.buscarLembretePorCreatedBy(teacherId);

		if (lembretes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<ReminderDTO> reminderDTOs = lembretes.stream()
				.map(lembrete -> new ReminderDTO(lembrete.getId(), lembrete.getConteudo(), lembrete.getHorarioSistema(),
						lembrete.getCreatedBy() != null ? lembrete.getCreatedBy().getNomeDocente()
								: (lembrete.getCreatedByInstitution() != null
										? lembrete.getCreatedByInstitution().getNameInstitution()
										: "Desconhecido"),
						lembrete.getCreatedBy() != null ? lembrete.getCreatedBy().getId()
								: (lembrete.getCreatedByInstitution() != null
										? lembrete.getCreatedByInstitution().getId()
										: null),
						lembrete.getInitials(), lembrete.getClassSt() != null ? lembrete.getClassSt().getId() : null))
				.collect(Collectors.toList());

		return new ResponseEntity<>(reminderDTOs, HttpStatus.OK);
	}

}
