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

	record ClassDTO(String nomeTurma, Long id, int quantidadeAlunos) {
	}

	record ClassDTOSimples(String nomeTurma, Long id) {
	}

	record TeacherDTO(String nomeDocente, Date dataNascimentoDocente, String emailDocente, String telefoneDocente,
			String identifierCode, String password, List<Long> disciplineId, String imageUrl) {
	}

	record TeacherDTOGet(Long id, String nomeDocente, String dataNascimentoDocente, String emailDocente,
			String telefoneDocente) {
	}

	record TeacherDTOTwo(String nomeDocente, String dataNascimentoDocente, String emailDocente, String telefoneDocente,
			String identifierCode, Long id, List<DisciplineDTO> disciplinas, List<ClassDTO> classes) {
	}

	record TeacherDTOTwoSimples(String nomeDocente, String dataNascimentoDocente, String emailDocente,
			String telefoneDocente, String identifierCode, Long id, List<DisciplineDTO> disciplinas,
			List<ClassDTOSimples> classes) {
	}

	record TeacherDTOTre(String nomeDocente, Long id, List<ClassDTO> classes) {
	}

	record TeacherDTOSimples(String nomeDocente, Long id, List<ClassDTOSimples> classes) {
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

	
	

	@PostMapping("/teacher")
	@PreAuthorize("hasRole('INSTITUTION')")
	public ResponseEntity<?> criarTeacher(@RequestBody TeacherDTO teacherDTO) {
		try {
			validarTeacherDTO(teacherDTO);

			List<Discipline> disciplines = disciplineRepo.findAllById(teacherDTO.disciplineId());
			if (disciplines.size() != teacherDTO.disciplineId().size()) {
				return ResponseEntity.badRequest().body(Map.of("error", "Algumas disciplinas não foram encontradas"));
			}

			Teacher teacher = new Teacher();
			teacher.setNomeDocente(teacherDTO.nomeDocente());
			teacher.setDataNascimentoDocente(teacherDTO.dataNascimentoDocente());
			teacher.setEmailDocente(teacherDTO.emailDocente());
			teacher.setTelefoneDocente(teacherDTO.telefoneDocente());
			teacher.setIdentifierCode(teacherDTO.identifierCode());
			teacher.setImageUrl(teacherDTO.imageUrl());
			teacher.setPassword(teacherDTO.password());
			teacher.setDisciplines(new ArrayList<>(disciplines));

			Teacher savedTeacher = teacherService.criarTeacher(teacher);

			return ResponseEntity.status(HttpStatus.CREATED).body(savedTeacher);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("error", "Erro ao criar professor: " + e.getMessage()));
		}
	}

	private void validarTeacherDTO(TeacherDTO teacherDTO) {
		if (teacherDTO.nomeDocente.isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha com um nome.");
		}
		if (teacherDTO.dataNascimentoDocente == null) {
			throw new IllegalArgumentException("Por favor preencha a data de nascimento.");
		}
		if (teacherDTO.emailDocente.isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha o campo email.");
		}
		if (!teacherDTO.emailDocente.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			throw new IllegalArgumentException("O email fornecido não tem formato válido.");
		}
		if (teacherRepo.existsByEmailDocente(teacherDTO.emailDocente())) {
			throw new IllegalArgumentException("Email já cadastrado.");
		} else if (studentRepo.existsByEmailAluno(teacherDTO.emailDocente())) {
			throw new IllegalArgumentException("Email já cadastrado.");
		}

		if (!teacherDTO.telefoneDocente().matches("\\d{11}")) {
			throw new IllegalArgumentException("Telefone deve conter exatamente 11 dígitos numéricos.");
		}
		if (teacherRepo.existsByTelefoneDocente(teacherDTO.telefoneDocente())) {
			throw new IllegalArgumentException("Telefone já cadastrado.");
		}

		if (teacherDTO.disciplineId.isEmpty()) {
			throw new IllegalArgumentException("Por favor preencha com no mínimo uma disciplina.");
		}

	}

	@GetMapping("/teacher")
	public ResponseEntity<?> buscarTeachers() {
		List<Teacher> teachers = teacherService.buscarTeachers();
		if (teachers.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Nenhum professor encontrado."));
		}

		List<TeacherDTOGet> teacherDTOs = teachers.stream().map(t -> new TeacherDTOGet(t.getId(),t.getNomeDocente(),
				t.getDataNascimentoDocente().toString(), t.getEmailDocente(), t.getTelefoneDocente()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(teacherDTOs);
	}

	@GetMapping("/teacher/classes/{id}")
	public ResponseEntity<TeacherDTOTre> buscarTeacherClassUnico(@PathVariable Long id) {
		Teacher buscarUnico = teacherService.buscarUnicoTeacher(id);

		if (buscarUnico != null) {
			List<ClassDTO> classes = buscarUnico.getTeachers().stream()
					.map(classe -> new ClassDTO(classe.getNomeTurma(), classe.getId(), classe.getStudents().size()))
					.collect(Collectors.toList());
			TeacherDTOTre teacherTree = new TeacherDTOTre(buscarUnico.getNomeDocente(), buscarUnico.getId(), classes);
			return ResponseEntity.ok(teacherTree);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

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
		TeacherDTOSimples TeacherDTOSimples = new TeacherDTOSimples(teacher.getNomeDocente(), teacher.getId(), classes);

		TeacherDTOTwoSimples teacherDTOTwoSimples = new TeacherDTOTwoSimples(teacher.getNomeDocente(),
				teacher.getDataNascimentoDocente().toString(), teacher.getEmailDocente(), teacher.getTelefoneDocente(),
				teacher.getIdentifierCode(), teacher.getId(), disciplines, classes);
		return ResponseEntity.ok(teacherDTOTwoSimples);
	}

	@PostMapping("/teacher/login")
	public ResponseEntity<Map<String, String>> loginTeacher(@RequestBody LoginTeacherDTO loginTeacherDTO) {
		String token = teacherService.loginTeacher(loginTeacherDTO.identifierCode(), loginTeacherDTO.password());
		return ResponseEntity.ok(Map.of("token", token));
	}

	@PutMapping("/teacher/{id}")
	public ResponseEntity<?> editarTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
		try {
			Teacher atualizado = teacherService.atualizarTeacher(id, teacher);
			if (atualizado == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Professor não encontrado"));
			}
			return ResponseEntity.ok(atualizado);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", "Erro ao atualizar professor: " + e.getMessage()));
		}
	}

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
	
	
	// Serviço que realizará o upload da imagem
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

		// Extrai o token do cabeçalho Authorization
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

		// Método para extrair o token do cabeçalho Authorization
		private String extractTokenFromHeader(String authHeader) {
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				return authHeader.substring(7);
			}
			return null;
		}

	
	
	
	
}
