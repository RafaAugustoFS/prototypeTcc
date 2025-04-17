package com.onAcademy.tcc.controller;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.onAcademy.tcc.dto.LoginTeacherDTO;
import com.onAcademy.tcc.dto.TeacherDTO;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.repository.DisciplineRepo;
import com.onAcademy.tcc.repository.StudentRepo;
import com.onAcademy.tcc.repository.TeacherRepo;
import com.onAcademy.tcc.service.ImageUploaderService;
import com.onAcademy.tcc.service.TeacherService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Teacher", description = "EndPoint de professor")
@RestController
@RequestMapping("/api")
public class TeacherController {

	record DisciplineDTO(String nomeDisciplina, Long discipline_id) {
	}

	record StudentDTO(Long id, String nomeAluno) {
	}

	record ClassDTO(String nomeTurma, Long id, int quantidadeAlunos) {
	}

	record TeacherDTOFeedback(Long id, String nomeDocente) {
	}

	record ClassDTOSimples(String nomeTurma, Long id) {
	}

	record TeacherDisciplineDTO(String nomeDocente, String emailDocente, String telefoneDocente,
			List<DisciplineDTO> disciplines) {
	}


	record TeacherDTOGet(Long id, String nomeDocente, String dataNascimentoDocente, String emailDocente,
			String telefoneDocente, String imageUrl) {
	}

	record TeacherDTOTwo(String nomeDocente, String dataNascimentoDocente, String emailDocente, String telefoneDocente,
			String identifierCode, Long id, List<DisciplineDTO> disciplinas, List<ClassDTO> classes) {
	}

	record TeacherDTOTwoSimples(String nomeDocente, String dataNascimentoDocente, String emailDocente,
			String telefoneDocente, String identifierCode, Long id, String imageUrl, List<DisciplineDTO> disciplinas,
			List<ClassDTOSimples> classes, List<FeedbackDTO> feedbacks) {
	}

	record TeacherDTOTre(String nomeDocente, Long id, String imageUrl, List<ClassDTO> classes) {
	}

	record FeedbackDTO(Long id, String conteudo, StudentDTO createdBy, TeacherDTOFeedback recipientTeacher) {
	}

	record TeacherDTOSimples(String nomeDocente, Long id, String imageUrl, List<ClassDTOSimples> classes,
			List<FeedbackDTO> feedback) {
	}

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private TeacherRepo teacherRepo;

	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private DisciplineRepo disciplineRepo;

	@Autowired
	private ImageUploaderService imageUploaderService;

	/**
	 * Endpoint para criar um novo professor.
	 * 
	 * @param teacherDTO Objeto contendo as informações do professor a ser criado.
	 * @return Status da criação do professor.
	 */
	@PreAuthorize("hasRole('INSTITUTION')")
	@PostMapping("/teacher")
	public ResponseEntity<?> criarTeacher(@RequestBody TeacherDTO teacherDTO) {
		try {
			Teacher teacherCriado = teacherService.criarTeacher(teacherDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(teacherCriado);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("error", "Erro ao criar professor: " + e.getMessage()));
		}
	}
	/**
	 * Valida os campos do DTO do professor.
	 * 
	 * @param teacherDTO O DTO do professor a ser validado.
	 * @throws IllegalArgumentException Se algum campo estiver inválido.
	 */

	private void validarTeacherPutDTO(Teacher teacher) {
		if (teacher.getNomeDocente().isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha com um nome.");
		}

		if (!teacher.getNomeDocente().matches("[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ\\s]+")) {
			throw new IllegalArgumentException("O nome deve conter apenas letras.");
		}

		if (teacher.getNomeDocente().length() < 2 || teacher.getNomeDocente().length() > 30) {
			throw new IllegalArgumentException("O nome deve ter entre 2 e 30 caracteres.");
		}

		if (teacher.getDataNascimentoDocente() == null) {
			throw new IllegalArgumentException("Por favor preencha a data de nascimento.");
		}
		if (teacher.getEmailDocente().isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha o campo email.");
		}
		if (!teacher.getEmailDocente().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			throw new IllegalArgumentException("O email fornecido não tem formato válido.");
		}
		if (teacherRepo.existsByEmailDocente(teacher.getEmailDocente())) {
			throw new IllegalArgumentException("Email já cadastrado.");
		} else if (studentRepo.existsByEmailAluno(teacher.getEmailDocente())) {
			throw new IllegalArgumentException("Email já cadastrado.");
		}

		if (!teacher.getTelefoneDocente().matches("\\d{11}")) {
			throw new IllegalArgumentException("Telefone deve conter exatamente 11 dígitos numéricos.");
		}
		if (teacherRepo.existsByTelefoneDocente(teacher.getTelefoneDocente())) {
			throw new IllegalArgumentException("Telefone já cadastrado.");
		}

		if (teacher.getDisciplines().isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha com no mínimo uma disciplina.");
		}

	}

	/**
	 * Endpoint para buscar todos os professores.
	 * 
	 * @return Lista de professores.
	 */
	@GetMapping("/teacher")
	public ResponseEntity<?> buscarTeachers() {
		List<Teacher> teachers = teacherService.buscarTeachers();
		if (teachers.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Nenhum professor encontrado."));
		}

		List<TeacherDTOGet> teacherDTOs = teachers.stream().map(t -> new TeacherDTOGet(t.getId(), t.getNomeDocente(),
				t.getDataNascimentoDocente().toString(), t.getEmailDocente(), t.getTelefoneDocente(), t.getImageUrl()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(teacherDTOs);
	}

	/**
	 * Este método busca um único professor através do ID fornecido e retorna as
	 * turmas que o professor leciona, com detalhes sobre o nome da turma, o ID e a
	 * quantidade de alunos em cada turma.
	 *
	 * @param id O ID do professor a ser buscado.
	 * @return ResponseEntity com os dados do professor e suas turmas, ou um erro
	 *         404 caso o professor não seja encontrado.
	 */
	@GetMapping("/teacher/classes/{id}")
	public ResponseEntity<TeacherDTOTre> buscarTeacherClassUnico(@PathVariable Long id) {
		Teacher buscarUnico = teacherService.buscarUnicoTeacher(id);

		if (buscarUnico != null) {
			List<ClassDTO> classes = buscarUnico.getTeachers().stream()
					.map(classe -> new ClassDTO(classe.getNomeTurma(), classe.getId(), classe.getStudents().size()))
					.collect(Collectors.toList());
			TeacherDTOTre teacherTree = new TeacherDTOTre(buscarUnico.getNomeDocente(), buscarUnico.getId(),
					buscarUnico.getImageUrl(), classes);
			return ResponseEntity.ok(teacherTree);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	/**
	 * Endpoint para buscar um professor e suas respectivas disciplinas, turmas e
	 * feedbacks.
	 * 
	 * @param id O ID do professor a ser buscado.
	 * @return Informações detalhadas sobre o professor.
	 */
	@GetMapping("/teacher/{id}")
	public ResponseEntity<?> buscarTeacherUnico(@PathVariable Long id) {
		Teacher teacher = teacherService.buscarUnicoTeacher(id);
		if (teacher == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Professor não encontrado"));
		}

		List<DisciplineDTO> disciplines = teacher.getDisciplines().stream()
				.map(d -> new DisciplineDTO(d.getNomeDisciplina(), d.getId())).collect(Collectors.toList());
		List<ClassDTOSimples> classes = teacher.getTeachers().stream()
				.map(classe -> new ClassDTOSimples(classe.getNomeTurma(), classe.getId())).collect(Collectors.toList());
		List<FeedbackDTO> feedbacks = teacher.getFeedback().stream()
				.map(feedback -> new FeedbackDTO(feedback.getId(), feedback.getConteudo(),
						new StudentDTO(feedback.getCreatedBy().getId(), feedback.getCreatedBy().getNomeAluno()), // Conversão
																													// correta
						new TeacherDTOFeedback(feedback.getRecipientTeacher().getId(),
								feedback.getRecipientTeacher().getNomeDocente())))
				.collect(Collectors.toList());

		TeacherDTOTwoSimples teacherDTOTwoSimples = new TeacherDTOTwoSimples(teacher.getNomeDocente(),
				teacher.getDataNascimentoDocente().toString(), teacher.getEmailDocente(), teacher.getTelefoneDocente(),
				teacher.getIdentifierCode(), teacher.getId(), teacher.getImageUrl(), disciplines, classes, feedbacks);
		return ResponseEntity.ok(teacherDTOTwoSimples);
	}

	@GetMapping("/teacher/discipline/{id}")
	public ResponseEntity<?> buscarTeacherUnicoDiscipline(@PathVariable Long id) {
		Teacher teacher = teacherService.buscarUnicoTeacher(id);
		if (teacher == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Professor não encontrado"));
		}
		List<DisciplineDTO> disciplines = teacher.getDisciplines().stream()
				.map(d -> new DisciplineDTO(d.getNomeDisciplina(), teacher.getId())).collect(Collectors.toList());

		TeacherDisciplineDTO teacherDisciplineDTO = new TeacherDisciplineDTO(teacher.getNomeDocente(),
				teacher.getEmailDocente(), teacher.getTelefoneDocente(), disciplines);
		return ResponseEntity.ok(teacherDisciplineDTO);
	}

	/**
	 * Endpoint para login de professor.
	 * 
	 * @param loginTeacherDTO Objeto contendo as credenciais de login.
	 * @return Token de autenticação JWT.
	 */
	@PostMapping("/teacher/login")
	public ResponseEntity<Map<String, String>> loginTeacher(@RequestBody LoginTeacherDTO loginTeacherDTO) {
		String token = teacherService.loginTeacher(loginTeacherDTO.identifierCode(), loginTeacherDTO.password());
		return ResponseEntity.ok(Map.of("token", token));
	}

	/**
	 * Endpoint para editar as informações de um professor existente.
	 * 
	 * @param id      O ID do professor a ser editado.
	 * @param teacher Objeto com as novas informações do professor.
	 * @return O professor atualizado.
	 */

	@PutMapping("/teacher/{id}")
	public ResponseEntity<?> editarTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
		try {
			validarTeacherPutDTO(teacher);
			Teacher atualizado = teacherService.atualizarTeacher(id, teacher);
			if (atualizado == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Professor não encontrado"));
			}
			return ResponseEntity.ok(atualizado);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("erro", "Dados inválidos", "detalhes", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", "Erro ao atualizar professor: " + e.getMessage()));
		}
	}

	/**
	 * Endpoint para deletar um professor pelo ID.
	 * 
	 * @param id O ID do professor a ser deletado.
	 * @return Status da operação de exclusão.
	 */
	@DeleteMapping("/teacher/{id}")
	public ResponseEntity<?> deletarTeacher(@PathVariable Long id) {
		try {
			Teacher deletado = teacherService.deletarTeacher(id);
			if (deletado == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Professor não encontrado"));
			}
			return ResponseEntity.ok(deletado);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", "Erro ao deletar professor: " + e.getMessage()));
		}
	}

	/**
	 * Endpoint para realizar o upload de uma imagem para um professor.
	 * 
	 * @param id      O ID do professor.
	 * @param request Contém a imagem codificada em base64.
	 * @return URL da imagem carregada.
	 */
	@PostMapping("/teacher/upload-image/{id}")
	public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestBody Map<String, String> request) {
		try {
			String base64Image = request.get("image");

			String imageUrl = imageUploaderService.uploadBase64Image(base64Image);

			Teacher teacher = teacherService.buscarUnicoTeacher(id);
			if (teacher == null) {
				return new ResponseEntity<>(Map.of("error", "Professor não encontrado"), HttpStatus.NOT_FOUND);
			}
			teacher.setImageUrl(imageUrl);
			teacherService.atualizarTeacher(id, teacher);

			return new ResponseEntity<>(Map.of("imageUrl", imageUrl), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao fazer upload da imagem: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Endpoint para recuperar a imagem de um professor.
	 * 
	 * @param id         O ID do professor.
	 * @param authHeader Cabeçalho de autenticação contendo o token JWT.
	 * @return A URL da imagem do professor.
	 */
	@GetMapping("/teacher/image/{id}")
	public ResponseEntity<?> getImage(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
		try {

			String token = extractTokenFromHeader(authHeader);
			if (token == null) {
				return new ResponseEntity<>(Map.of("error", "Token não fornecido"), HttpStatus.UNAUTHORIZED);
			}

			Teacher teacher = teacherService.buscarUnicoTeacher(id);
			if (teacher == null) {
				return new ResponseEntity<>(Map.of("error", "Estudante não encontrado"), HttpStatus.NOT_FOUND);
			}

			String imageUrl = teacher.getImageUrl();
			if (imageUrl == null || imageUrl.isEmpty()) {
				return new ResponseEntity<>(Map.of("error", "Imagem não encontrada"), HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(Map.of("imageUrl", imageUrl), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao recuperar a imagem: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Método auxiliar para extrair o token do cabeçalho Authorization.
	 * 
	 * @param authHeader Cabeçalho de autenticação.
	 * @return O token JWT ou null se não estiver presente.
	 */
	private String extractTokenFromHeader(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

}
