package com.onAcademy.tcc.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onAcademy.tcc.model.FeedBackByStudent;

import com.onAcademy.tcc.service.FeedbackByStudentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Feedback", description = "EndPoint de feedback")
@RestController
@RequestMapping("/api")
public class FeedBackByStudentController {

	@Autowired
	private FeedbackByStudentService feedbackByStudentService;

	record TeacherDTO(String nomeDocente, Long id) {
	}

	record CreatedByDTO(String nomeAluno, Long id) {
	}

	record FeedbackDTO(String conteudo, CreatedByDTO createdBy, TeacherDTO teacher) {
	}

	record RecipientDTO(Long id, CreatedByDTO aluno, String conteudo) {
	}

	/**
	 * Cria um feedback de um aluno para um professor.
	 * 
	 * - Recebe os dados do feedback via requisição HTTP POST. - Valida o conteúdo
	 * do feedback e, se válido, cria o feedback no banco de dados. - Se a criação
	 * for bem-sucedida, retorna uma resposta HTTP 201 (Created). - Caso contrário,
	 * retorna uma resposta de erro HTTP 400 (Bad Request).
	 * 
	 * @param feedbackByStudent Objeto contendo o feedback a ser criado.
	 * @return Resposta HTTP com o feedback criado ou erro.
	 */
	@PostMapping("/feedbackStudent")
	public ResponseEntity<?> criarFeedback(@RequestBody FeedBackByStudent feedbackByStudent) {
		try {
			validarFeedBackByStudent(feedbackByStudent);
			FeedBackByStudent feedback1 = feedbackByStudentService.criarFeedbackStudent(feedbackByStudent);
			return new ResponseEntity<>(feedback1, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao criar feedback: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Valida o conteúdo de um feedback.
	 * 
	 * - Verifica se o conteúdo do feedback não está vazio. - Se o conteúdo estiver
	 * vazio, lança uma exceção com mensagem de erro.
	 * 
	 * @param feedbackByStudent O feedback a ser validado.
	 */
	public void validarFeedBackByStudent(FeedBackByStudent feedbackByStudent) {
		if (feedbackByStudent.getConteudo().isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha todos os campos.");
		}
	}

	/**
	 * Busca todos os feedbacks fornecidos pelos alunos.
	 * 
	 * - Retorna uma lista de todos os feedbacks no sistema. - Caso haja algum erro
	 * ao buscar os feedbacks, retorna uma resposta de erro HTTP 500.
	 * 
	 * @return Resposta HTTP com a lista de feedbacks ou erro.
	 */
	@GetMapping("/feedbackStudent")
	public ResponseEntity<?> buscarTodasFeedback() {
		try {
			List<FeedBackByStudent> feedbacks = feedbackByStudentService.buscarTodosFeedbacksStudent();
			return new ResponseEntity<>(feedbacks, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar feedbacks: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca um feedback específico fornecido por um aluno.
	 * 
	 * - Recebe o ID do feedback e retorna o feedback correspondente. - Se o
	 * feedback não for encontrado, retorna uma resposta de erro HTTP 404 (Not
	 * Found). - Se houver erro ao buscar o feedback, retorna uma resposta de erro
	 * HTTP 400.
	 * 
	 * @param id ID do feedback a ser buscado.
	 * @return Resposta HTTP com o feedback encontrado ou erro.
	 */
	@GetMapping("/feedbackStudent/{id}")
	public ResponseEntity<?> buscarFeedback(@PathVariable Long id) {
		try {
			FeedBackByStudent buscarFeedback = feedbackByStudentService.buscarFeedbackUnicoStudent(id);

			if (buscarFeedback == null) {
				return new ResponseEntity<>(Map.of("error", "Feedback não encontrado"), HttpStatus.NOT_FOUND);
			}

			var createdBy = new CreatedByDTO(buscarFeedback.getCreatedBy().getNomeAluno(),
					buscarFeedback.getCreatedBy().getId());
			var teacher = new TeacherDTO(buscarFeedback.getRecipientTeacher().getNomeDocente(),
					buscarFeedback.getRecipientTeacher().getId());
			var feedbackDTO = new FeedbackDTO(buscarFeedback.getConteudo(), createdBy, teacher);

			return new ResponseEntity<>(feedbackDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar feedback: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Busca todos os feedbacks de um professor específico.
	 * 
	 * - Recebe o ID do professor e retorna todos os feedbacks enviados a ele. - Se
	 * não houver feedbacks para o professor, retorna uma resposta HTTP 404 (Not
	 * Found). - Se houver erro ao buscar os feedbacks, retorna uma resposta de erro
	 * HTTP 500.
	 * 
	 * @param recipientTeacherId ID do professor destinatário dos feedbacks.
	 * @return Resposta HTTP com a lista de feedbacks do professor ou erro.
	 */
	@GetMapping("feedbackStudent/teacher/{recipientTeacherId}")
	public ResponseEntity<List<RecipientDTO>> buscarFeedbacksDocente(@PathVariable Long recipientTeacherId) {
		List<FeedBackByStudent> feedbacks = feedbackByStudentService.buscarFeedbacksDocente(recipientTeacherId);

		if (feedbacks.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<RecipientDTO> recipientDTO = feedbacks.stream().map(feedback -> {
			CreatedByDTO teacherDTO = new CreatedByDTO(
					feedback.getCreatedBy().getNomeAluno() != null ? feedback.getCreatedBy().getNomeAluno() : null,
					feedback.getCreatedBy() != null ? feedback.getCreatedBy().getId() : null);
			return new RecipientDTO(feedback.getId(), teacherDTO, feedback.getConteudo());
		}).collect(Collectors.toList());

		return new ResponseEntity<>(recipientDTO, HttpStatus.OK);
	}
}
