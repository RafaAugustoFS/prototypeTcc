package com.onAcademy.tcc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onAcademy.tcc.config.TokenProvider;
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

	record ClassDTO(String nomeTurma, Long idTurma) {
	};

	record NoteDTO(Long idNota, Double nota, int bimestre, String status, String nomeDisciplina) {
	};

	record StudentDTO(String nome, String dataNascimentoAluno, String telefoneAluno, String emailAluno,
			String matriculaAluno, ClassDTO turma, List<NoteDTO> notas) {
	};

	@PostMapping("/student/login")
	public ResponseEntity<Map<String, String>> loginStudent(@RequestBody LoginStudent loginStudent) {
		try {
			String token = studentService.loginStudent(loginStudent.identifierCode(), loginStudent.password());
			Map<String, String> response = new HashMap<>();
			response.put("token", token);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Falha no login: " + e.getMessage()), HttpStatus.UNAUTHORIZED);
		}
	}

	@PreAuthorize("hasRole('INSTITUTION')")
	@PostMapping("/student")
	public ResponseEntity<?> criarEstudante(@RequestBody StudentClassDTO studentDTO) {
		try {
			Student student1 = studentService.criarEstudante(studentDTO);
			return new ResponseEntity<>(student1, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao criar estudante: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/student")
	public ResponseEntity<?> buscarTodosEstudantes() {
		try {
			List<Student> students = studentService.buscarTodosEstudantes();
			return new ResponseEntity<>(students, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar estudantes: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/student/{id}")
	public ResponseEntity<?> buscarEstudanteUnico(@PathVariable Long id) {
		try {
			Student buscaEstudante = studentService.buscarEstudanteUnico(id);
			if (buscaEstudante != null) {
				List<NoteDTO> notas = buscaEstudante
						.getNotas().stream().map(nota -> new NoteDTO(nota.getId(), nota.getNota(), nota.getBimestre(),
								nota.getStatus(), nota.getDisciplineId().getNomeDisciplina()))
						.collect(Collectors.toList());

				var turma = new ClassDTO(buscaEstudante.getClassSt().getNomeTurma(),
						buscaEstudante.getClassSt().getId());
				var studentDTO = new StudentDTO(buscaEstudante.getNomeAluno(),
						buscaEstudante.getDataNascimentoAluno().toString(), buscaEstudante.getTelefoneAluno(),
						buscaEstudante.getEmailAluno(), buscaEstudante.getIdentifierCode(), turma, notas);

				return new ResponseEntity<>(studentDTO, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(Map.of("error", "Estudante não encontrado"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar estudante: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/student/{id}")
	public ResponseEntity<?> atualizarEstudante(@PathVariable Long id, @RequestBody Student student) {
		try {
			Student atualizarEstudante = studentService.atualizarEstudante(id, student);
			if (atualizarEstudante != null) {
				return new ResponseEntity<>(atualizarEstudante, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(Map.of("error", "Estudante não encontrado"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao atualizar estudante: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/student/{id}")
	public ResponseEntity<?> deletarEstudante(@PathVariable Long id) {
		try {
			Student deletarEstudante = studentService.deletarEstudante(id);
			if (deletarEstudante != null) {
				return new ResponseEntity<>(deletarEstudante, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(Map.of("error", "Estudante não encontrado"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao deletar estudante: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}

	// Serviço que realizará o upload da imagem
	@PostMapping("/student/upload-image/{id}")
	public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestBody Map<String, String> request) {
		try {
			String base64Image = request.get("image");

			String imageUrl = imageUploaderService.uploadBase64Image(base64Image);

			Student student = studentService.buscarEstudanteUnico(id);
			if (student == null) {
				return new ResponseEntity<>(Map.of("error", "Estudante não encontrado"), HttpStatus.NOT_FOUND);
			}
			student.setImageUrl(imageUrl);
			studentService.atualizarEstudante(id, student);

			return new ResponseEntity<>(Map.of("imageUrl", imageUrl), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao fazer upload da imagem: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	// Extrai o token do cabeçalho Authorization
	@GetMapping("/student/image/{id}")
	public ResponseEntity<?> getImage(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
		try {

			String token = extractTokenFromHeader(authHeader);
			if (token == null) {
				return new ResponseEntity<>(Map.of("error", "Token não fornecido"), HttpStatus.UNAUTHORIZED);
			}

			Student student = studentService.buscarEstudanteUnico(id);
			if (student == null) {
				return new ResponseEntity<>(Map.of("error", "Estudante não encontrado"), HttpStatus.NOT_FOUND);
			}

			String imageUrl = student.getImageUrl();
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
