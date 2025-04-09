package com.onAcademy.tcc.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.onAcademy.tcc.dto.LoginStudent;
import com.onAcademy.tcc.dto.StudentClassDTO;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.service.ImageUploaderService;
import com.onAcademy.tcc.service.StudentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Student", description = "EndPoint de estudante")
@RestController
@RequestMapping("/api")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private ImageUploaderService imageUploaderService;

	/**
	 * DTO para representar uma turma de forma simplificada.
	 */
	record DisciplinaDTO(Long id, String nomeDisciplina) {}
	
	record ClassDTO(String nomeTurma, Long idTurma, List<DisciplinaDTO> disciplinaTurmas) {
	}

	/**
	 * DTO para representar uma nota de forma simplificada.
	 */
	record NoteDTO(Long idNota, Double nota, int bimestre, String status, String nomeDisciplina) {
	}

	/**
	 * DTO para representar um estudante de forma simplificada.
	 */
	record StudentDTO(String nome, String dataNascimentoAluno, String telefoneAluno, String emailAluno,
			String matriculaAluno, String imageUrl, ClassDTO turma, List<NoteDTO> notas) {
	}

	/**
	 * Realiza o login de um estudante.
	 *
	 * @param loginStudent DTO contendo o código de identificação e a senha do
	 *                     estudante.
	 * @return ResponseEntity com o token de autenticação ou uma mensagem de erro.
	 */
	@PostMapping("/student/login")
	public ResponseEntity<Map<String, String>> loginStudent(@RequestBody LoginStudent loginStudent) {
		try {
			String token = studentService.loginStudent(loginStudent.identifierCode(), loginStudent.password());
			return ResponseEntity.ok(Map.of("token", token));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "Falha no login: " + e.getMessage()));
		}
	}

	/**
	 * Cria um novo estudante.
	 *
	 * @param studentDTO DTO contendo os dados do estudante.
	 * @return ResponseEntity com o estudante criado ou uma mensagem de erro.
	 */
	@PreAuthorize("hasRole('INSTITUTION')")
	@PostMapping("/student")
	public ResponseEntity<?> criarEstudante(@RequestBody StudentClassDTO studentDTO) {
		try {
			Student estudanteCriado = studentService.criarEstudante(studentDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(estudanteCriado);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", "Erro ao criar estudante: " + e.getMessage()));
		}
	}

	/**
	 * Retorna uma lista de todos os estudantes.
	 *
	 * @return ResponseEntity com a lista de estudantes ou uma mensagem de erro.
	 */
	@GetMapping("/student")
	public ResponseEntity<?> buscarTodosEstudantes() {
		try {
			List<Student> estudantes = studentService.buscarTodosEstudantes();
			return ResponseEntity.ok(estudantes);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Erro ao buscar estudantes: " + e.getMessage()));
		}
	}

	/**
	 * Busca um estudante pelo ID.
	 *
	 * @param id ID do estudante a ser buscado.
	 * @return ResponseEntity com o estudante no formato DTO ou uma mensagem de
	 *         erro.
	 */
	@GetMapping("/student/{id}")
	public ResponseEntity<?> buscarEstudantePorId(@PathVariable Long id) {
		try {
			Student estudante = studentService.buscarEstudanteUnico(id);
			if (estudante == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Estudante não encontrado"));
			}

			List<NoteDTO> notas = estudante.getNotas().stream().map(nota -> new NoteDTO(nota.getId(), nota.getNota(),
					nota.getBimestre(), nota.getStatus(), nota.getDisciplineId().getNomeDisciplina()))
					.collect(Collectors.toList());

			ClassDTO turma = null;
			if (estudante.getClassSt() != null) {
			    List<DisciplinaDTO> disciplinasDTO = estudante.getClassSt().getDisciplinaTurmas()
			        .stream()
			        .map(d -> new DisciplinaDTO(d.getId(), d.getNomeDisciplina()))
			        .collect(Collectors.toList());

			    turma = new ClassDTO(
			        estudante.getClassSt().getNomeTurma(),
			        estudante.getClassSt().getId(),
			        disciplinasDTO
			    );
			}

			StudentDTO studentDTO = new StudentDTO(estudante.getNomeAluno(),
					estudante.getDataNascimentoAluno().toString(), estudante.getTelefoneAluno(),
					estudante.getEmailAluno(), estudante.getIdentifierCode(), estudante.getImageUrl(), turma, notas);

			return ResponseEntity.ok(studentDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Erro ao buscar estudante: " + e.getMessage()));
		}
	}

	/**
	 * Atualiza os dados de um estudante existente.
	 *
	 * @param id      ID do estudante a ser atualizado.
	 * @param student Objeto contendo os novos dados do estudante.
	 * @return ResponseEntity com o estudante atualizado ou uma mensagem de erro.
	 */
	@PutMapping("/student/{id}")
	public ResponseEntity<?> atualizarEstudante(@PathVariable Long id, @RequestBody Student student) {
		try {
			Student estudanteAtualizado = studentService.atualizarEstudante(id, student);
			if (estudanteAtualizado == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Estudante não encontrado"));
			}
			return ResponseEntity.ok(estudanteAtualizado);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", "Erro ao atualizar estudante: " + e.getMessage()));
		}
	}

	/**
	 * Exclui um estudante pelo ID.
	 *
	 * @param id ID do estudante a ser excluído.
	 * @return ResponseEntity com o estudante excluído ou uma mensagem de erro.
	 */
	@DeleteMapping("/student/{id}")
	public ResponseEntity<?> deletarEstudante(@PathVariable Long id) {
		try {
			Student estudanteDeletado = studentService.deletarEstudante(id);
			if (estudanteDeletado == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Estudante não encontrado"));
			}
			return ResponseEntity.ok(estudanteDeletado);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Erro ao deletar estudante: " + e.getMessage()));
		}
	}

	/**
	 * Realiza o upload da imagem de um estudante.
	 *
	 * @param id      ID do estudante.
	 * @param request Mapa contendo a imagem em formato base64.
	 * @return ResponseEntity com a URL da imagem ou uma mensagem de erro.
	 */
	@PostMapping("/student/upload-image/{id}")
	public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestBody Map<String, String> request) {
		try {
			String base64Image = request.get("image");
			if (base64Image == null || base64Image.trim().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Imagem não fornecida."));
			}

			String imageUrl = imageUploaderService.uploadBase64Image(base64Image);
			Student estudante = studentService.buscarEstudanteUnico(id);
			if (estudante == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Estudante não encontrado"));
			}

			estudante.setImageUrl(imageUrl);
			studentService.atualizarEstudante(id, estudante);

			return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", "Erro ao fazer upload da imagem: " + e.getMessage()));
		}
	}

	/**
	 * Recupera a URL da imagem de um estudante.
	 *
	 * @param id         ID do estudante.
	 * @param authHeader Cabeçalho de autorização contendo o token.
	 * @return ResponseEntity com a URL da imagem ou uma mensagem de erro.
	 */
	@GetMapping("/student/image/{id}")
	public ResponseEntity<?> getImage(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
		try {
			String token = extractTokenFromHeader(authHeader);
			if (token == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token não fornecido"));
			}

			Student estudante = studentService.buscarEstudanteUnico(id);
			if (estudante == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Estudante não encontrado"));
			}

			String imageUrl = estudante.getImageUrl();
			if (imageUrl == null || imageUrl.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Imagem não encontrada"));
			}

			return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Erro ao recuperar a imagem: " + e.getMessage()));
		}
	}

	/**
	 * Extrai o token do cabeçalho de autorização.
	 *
	 * @param authHeader Cabeçalho de autorização.
	 * @return Token extraído ou null se inválido.
	 */
	private String extractTokenFromHeader(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}
}