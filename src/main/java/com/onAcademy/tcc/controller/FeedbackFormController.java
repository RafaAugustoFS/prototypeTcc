package com.onAcademy.tcc.controller;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.onAcademy.tcc.model.FeedbackForm;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.repository.FeedbackFormRepo;
import com.onAcademy.tcc.repository.StudentRepo;
import com.onAcademy.tcc.service.FeedbackFormService;
 
import io.swagger.v3.oas.annotations.tags.Tag;
 
@Tag(name = "FeedbackForm", description = "EndPoint do formulário feedback")
@RestController
@RequestMapping("/api")
public class FeedbackFormController {
	@Autowired
	private FeedbackFormService feedbackFormService;
 
	@Autowired
	FeedbackFormRepo feedbackFormRepo;
 
	@Autowired
	StudentRepo studentRepo;
 
	record StudentDTO(String nomeAluno, Long id) {
	}
 
	record StudentDTOTurma(String nomeAluno, Long id, Long turmaId) {
	}
 
	record CreatedByDTO(String nomeDocente, Long id) {
	}
 
	record FeedbackDTO(int resposta1, int resposta2, int resposta3, int resposta4, int resposta5, int bimestre,
			CreatedByDTO createdByDTO, StudentDTO student) {
	}
 
	record FeedbackDTOTurma(int resposta1, int resposta2, int resposta3, int resposta4, int resposta5,
			CreatedByDTO createdByDTO, StudentDTOTurma student) {
	}
 
	record FeedbackMediasDTO(double mediaResposta1, double mediaResposta2, double mediaResposta3, double mediaResposta4,
			double mediaResposta5, int totalFeedbacks) {
	}
 
	/**
	 * Cria um feedback de um docente para um aluno.
	 *
	 * - Recebe os dados do feedback via requisição HTTP POST. - Valida o conteúdo
	 * do feedback, incluindo a existência de feedbacks anteriores para o mesmo
	 * aluno e bimestre. - Se o feedback for válido, cria o feedback no banco de
	 * dados. - Caso contrário, retorna uma resposta de erro HTTP 400 (Bad Request)
	 * ou HTTP 500 (Internal Server Error).
	 *
	 * @param feedbackByStudent Objeto contendo o feedback a ser criado.
	 * @return Resposta HTTP com o feedback criado ou erro.
	 */
	@PostMapping("/feedbackForm")
	public ResponseEntity<?> criarFeedback(@RequestBody FeedbackForm feedbackByStudent) {
 
		try {
			validarFeedback(feedbackByStudent);
 
			FeedbackForm feedback1 = feedbackFormService.criarFeedbackStudent(feedbackByStudent);
			return ResponseEntity.status(HttpStatus.CREATED).body(feedback1);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("error", "Erro ao criar professor: " + e.getMessage()));
		}
 
	}
 
	/**
	 * Valida o conteúdo de um feedback.
	 *
	 * - Verifica se o aluno para o qual o feedback está sendo dado existe. - Valida
	 * se o bimestre informado está entre 1 e 4. - Verifica se já existe um feedback
	 * registrado para o mesmo aluno e bimestre.
	 *
	 * @param feedbackByStudent O feedback a ser validado.
	 * @throws IllegalArgumentException Se houver algum erro de validação nos dados
	 *                                  do feedback.
	 * @throws RuntimeException         Se já existir um feedback para o aluno no
	 *                                  mesmo bimestre.
	 */
	private void validarFeedback(FeedbackForm feedbackByStudent) {
		Student student = studentRepo.findById(feedbackByStudent.getRecipientStudent().getId())
				.orElseThrow(() -> new RuntimeException("Estudante não encontrado."));
		if (feedbackByStudent.getRecipientStudent() == null) {
			new IllegalArgumentException("Precisa indicar para qual aluno é este feedback.");
		}
 
		if (feedbackByStudent.getBimestre() > 4 || feedbackByStudent.getBimestre() < 1) {
			throw new IllegalArgumentException("Bimestre inválido. Deve estar entre 1 e 4.");
		}
		boolean feedbackExists = feedbackFormRepo.existsByCreatedByAndRecipientStudentAndBimestre(
				feedbackByStudent.getCreatedBy(), feedbackByStudent.getRecipientStudent(),
				feedbackByStudent.getBimestre());
 
		if (feedbackExists) {
			throw new RuntimeException("Já existe um feedback para este aluno nesse bimestre.");
		}
 
	}
 
	/**
	 * Busca os feedbacks fornecidos a um aluno específico.
	 *
	 * - Recebe o ID de um aluno e retorna todos os feedbacks fornecidos a ele. - Se
	 * o aluno não tiver feedbacks registrados, retorna uma resposta HTTP 404 (Not
	 * Found). - Retorna uma lista de feedbacks convertidos para DTOs.
	 *
	 * @param id ID do aluno para o qual os feedbacks são solicitados.
	 * @return Resposta HTTP com a lista de feedbacks ou erro.
	 */
	@GetMapping("/student/feedback/{id}")
	public ResponseEntity<List<FeedbackDTO>> buscarPorAluno(@PathVariable Long id) {
		List<FeedbackForm> feedbacks = feedbackFormService.buscarFeedbackPorAluno(id);
		if (feedbacks != null) {
			List<FeedbackDTO> feedbackDtos = feedbacks.stream()
					.map(feedback -> new FeedbackDTO(feedback.getResposta1(), feedback.getResposta2(),
							feedback.getResposta3(), feedback.getResposta4(), feedback.getResposta5(),
							feedback.getBimestre(),
							new CreatedByDTO(feedback.getCreatedBy().getNomeDocente(), feedback.getCreatedBy().getId()),
							new StudentDTO(feedback.getRecipientStudent().getNomeAluno(),
									feedback.getRecipientStudent().getId())))
					.toList();
			return new ResponseEntity<>(feedbackDtos, HttpStatus.OK);
		}
		return null;
	}
 
	// Todos
	@GetMapping("/class/feedbacks/{turmaId}")
	public ResponseEntity<List<FeedbackDTOTurma>> listarFeedbacksPorTurma(@PathVariable Long turmaId) {
		List<FeedbackForm> feedbacks = feedbackFormService.buscarFeedbacksComRespostasPorTurma(turmaId);
 
		List<FeedbackDTOTurma> feedbackDtos = feedbacks.stream()
				.map(feedback -> new FeedbackDTOTurma(feedback.getResposta1(), feedback.getResposta2(),
						feedback.getResposta3(), feedback.getResposta4(), feedback.getResposta5(),
						new CreatedByDTO(feedback.getCreatedBy().getNomeDocente(), feedback.getCreatedBy().getId()),
						new StudentDTOTurma(feedback.getRecipientStudent().getNomeAluno(),
								feedback.getRecipientStudent().getId(), feedback.getRecipientStudent().getTurmaId())))
				.toList();
 
		return new ResponseEntity<>(feedbackDtos, HttpStatus.OK);
	}
 
	// MÉDIA
	@GetMapping("/class/feedback/{turmaId}")
	public ResponseEntity<?> obterMediasFeedbacks(@PathVariable Long turmaId) {
		try {
			List<Double> medias = feedbackFormService.calcularMediasFeedbacksPorTurma(turmaId);
			List<FeedbackForm> feedbacks = feedbackFormService.buscarFeedbacksComRespostasPorTurma(turmaId);
 
			FeedbackMediasDTO response = new FeedbackMediasDTO(medias.get(0), medias.get(1), medias.get(2),
					medias.get(3), medias.get(4), feedbacks.size());
 
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
		}
	}
 
}
 