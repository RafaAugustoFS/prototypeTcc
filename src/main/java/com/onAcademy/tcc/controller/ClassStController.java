package com.onAcademy.tcc.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.repository.ClassStRepo;
import com.onAcademy.tcc.repository.DisciplineRepo;
import com.onAcademy.tcc.repository.TeacherRepo;
import com.onAcademy.tcc.service.ClassStService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;

@Tag(name = "Class", description = "EndPoint de turma")
@RestController
@RequestMapping("/api")
public class ClassStController {

	@Autowired
	private ClassStService classStService;

	@Autowired
	private ClassStRepo classStRepo;

	@Autowired
	private TeacherRepo teacherRepo;

	@Autowired
	private DisciplineRepo disRepo;

	record TeacherTurmaDTO(Long id, String nomeDocente) {
	}

	record DisciplineTurmaDTO(Long id, String nomeDisciplina) {
	}

	record NoteDTO(String nomeDisciplina, Double valorNota) {
	}

	record StudentDTO(String nomeAluno, String dataNascimentoAluno, List<NoteDTO> nota, Long id,
			String identifierCode) {
	}

	record TeacherDTO(String nome, Long id) {
	}

	record ClassDTO(String nomeTurma, int anoLetivoTurma, int capacidadeMaximaTurma, int salaTurma, String periodoTurma,
			List<Long> idTeacher, List<Long> disciplineId) {
	}

	record ClassUpdateDTO(String nomeTurma, int anoLetivoTurma, int capacidadeMaximaTurma, int salaTurma,
			String periodoTurma, List<Long> idTeacher, List<Long> disciplineId) {
	}

	record ClassDTODisciplina(String nomeTurma, String periodoTurma, List<Long> disciplineId,
			List<DisciplineTurmaDTO> disciplinas) {
	}

	record ClassDTOTwo(String nomeTurma, String periodoTurma, Long id, List<StudentDTO> students) {
	}

	record ClassDTOTre(Long id, String nomeTurma, String periodoTurma, int alunosAtivos,
			List<TeacherTurmaDTO> teachers) {
	}

	record ClassDisciplinasTeacherDTO(Long id, String nomeTurma, int anoLetivoTurma, String periodoTurma,
			int capacidadeMaximaTurma, int salaTurma, int quantidadeAlunos, List<TeacherTurmaDTO> teachers,
			List<DisciplineTurmaDTO> disciplines) {
	}

	/**
	 * Cria uma nova turma e associa professores e disciplinas.
	 * 
	 * - Recebe os dados da turma através de um DTO. - Valida os dados fornecidos. -
	 * Verifica se os professores e disciplinas existem. - Se algum dado estiver
	 * incorreto, retorna erro. - Se a turma for criada com sucesso, retorna a
	 * resposta com o status HTTP 201 (Created).
	 * 
	 * @param classDTO Objeto contendo os dados da turma a ser criada.
	 * @return Resposta com a turma criada ou erro.
	 */

	@PreAuthorize("hasRole('INSTITUTION')")
	@PostMapping("/class")
	@Transactional
	public ResponseEntity<?> criarClasse(@RequestBody ClassDTO classDTO) {
		System.out.println("Recebido: " + classDTO);
		try {
			List<Teacher> teacher = teacherRepo.findAllById(classDTO.idTeacher());
			List<Discipline> disciplines = disRepo.findAllById(classDTO.disciplineId());
			validarClassSt(classDTO);
			if (teacher.size() != classDTO.idTeacher().size()) {
				return new ResponseEntity<>(Map.of("error", "Professor não encontrado."), HttpStatus.BAD_REQUEST);
			}
			if (disciplines.size() != classDTO.disciplineId().size()) {
				return new ResponseEntity<>(Map.of("error", "Disciplina não encontrada."), HttpStatus.BAD_REQUEST);
			}

			ClassSt classSt = new ClassSt();
			classSt.setNomeTurma(classDTO.nomeTurma);
			classSt.setAnoLetivoTurma(classDTO.anoLetivoTurma);
			classSt.setPeriodoTurma(classDTO.periodoTurma);
			classSt.setCapacidadeMaximaTurma(classDTO.capacidadeMaximaTurma);
			classSt.setSalaTurma(classDTO.salaTurma);

			var st = classStRepo.save(classSt);
			st.setDisciplinaTurmas(disciplines);
			st.setClasses(teacher);

			classStRepo.save(st);
			return new ResponseEntity<>(classSt, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao criar classe: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Valida os dados fornecidos para a criação de uma turma.
	 * 
	 * - Verifica se os campos obrigatórios estão preenchidos corretamente. - Valida
	 * a capacidade máxima da turma e o número de sala. - Lança exceções caso os
	 * dados sejam inválidos.
	 * 
	 * @param classDTO Objeto contendo os dados da turma a ser validada.
	 * @throws IllegalArgumentException Se algum dado estiver inválido.
	 */
	public void validarClassSt(ClassDTO classDTO) {

		List<String> lista = new ArrayList<>();
		lista.add("Vespertino");
		lista.add("Matutino");
		lista.add("Noturno");
		lista.add("Integral");

		if (classDTO.nomeTurma.isEmpty()) {
			throw new IllegalArgumentException("Nome da turma é obrigatório.");
		}
		if (classDTO.anoLetivoTurma <= 2023) {
			throw new IllegalArgumentException("Ano letivo da turma inválido.");
		}
		if (classDTO.periodoTurma.isEmpty()) {
			throw new IllegalArgumentException("Periodo da turma é obrigatório.");
		}

		if (lista.stream().noneMatch(p -> p.equalsIgnoreCase(classDTO.periodoTurma))) {
			throw new IllegalArgumentException("Período inválido. Os períodos permitidos são: " + lista);
		}

		if (classDTO.capacidadeMaximaTurma <= 10 || classDTO.capacidadeMaximaTurma > 60) {
			throw new IllegalArgumentException("Por favor insira uma capacidade válida.");
		}
		if (classDTO.salaTurma <= 0) {
			throw new IllegalArgumentException("Por favor insira uma sala válida.");
		}
		if (classDTO.idTeacher.isEmpty()) {
			throw new IllegalArgumentException("Turma deve ter professores.");
		}

		if (classDTO.disciplineId().isEmpty()) {
			throw new IllegalArgumentException("Turma deve ter disciplinas.");

		}

	}

	public void validarClassStUpdate(ClassUpdateDTO classDTO) {
		List<String> lista = new ArrayList<>();
		lista.add("Vespertino");
		lista.add("Matutino");
		lista.add("Noturno");
		lista.add("Integral");

		if (classDTO.nomeTurma.isEmpty()) {
			throw new IllegalArgumentException("Nome da turma é obrigatório.");
		}
		if (classDTO.anoLetivoTurma <= 2023) {
			throw new IllegalArgumentException("Ano letivo da turma inválido.");
		}
		if (classDTO.periodoTurma.isEmpty()) {
			throw new IllegalArgumentException("Periodo da turma é obrigatório.");
		}

		if (lista.stream().noneMatch(p -> p.equalsIgnoreCase(classDTO.periodoTurma))) {
			throw new IllegalArgumentException("Período inválido. Os períodos permitidos são: " + lista);
		}

		if (classDTO.capacidadeMaximaTurma <= 10 || classDTO.capacidadeMaximaTurma > 60) {
			throw new IllegalArgumentException("Por favor insira uma capacidade válida.");
		}
		if (classDTO.salaTurma <= 0) {
			throw new IllegalArgumentException("Por favor insira uma sala válida.");
		}
		if (classDTO.idTeacher.isEmpty()) {
			throw new IllegalArgumentException("Turma deve ter professores.");
		}

		if (classDTO.disciplineId().isEmpty()) {
			throw new IllegalArgumentException("Turma deve ter disciplinas.");

		}

	}

	/**
	 * Retorna todas as turmas cadastradas com seus respectivos professores.
	 * 
	 * - Consulta todas as turmas no sistema. - Converte as turmas para o formato de
	 * resposta utilizando DTOs. - Retorna a lista de turmas no formato JSON.
	 * 
	 * @return Resposta com a lista de turmas.
	 */
	@GetMapping("/class")
	public ResponseEntity<?> buscarTodasClasses() {
		try {
			List<ClassSt> classList = classStService.buscarTodasClasses();
			List<ClassDTOTre> classDTOList = classList.stream().map(classSt -> {
				List<TeacherTurmaDTO> teachers = classSt.getClasses().stream()
						.map(teacher -> new TeacherTurmaDTO(teacher.getId(), teacher.getNomeDocente()))
						.collect(Collectors.toList());
				return new ClassDTOTre(classSt.getId(), classSt.getNomeTurma(), classSt.getPeriodoTurma(),
						classSt.getStudents().size(), teachers);
			}).collect(Collectors.toList());
			return ResponseEntity.ok(classDTOList);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar todas as classes: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Retorna todas as turmas com suas respectivas disciplinas.
	 * 
	 * - Consulta todas as turmas no sistema. - Converte as turmas e disciplinas
	 * para o formato de resposta utilizando DTOs. - Retorna a lista de turmas com
	 * suas disciplinas.
	 * 
	 * @return Resposta com a lista de turmas e disciplinas.
	 */
	@GetMapping("/class/discipline")
	public ResponseEntity<List<ClassDTODisciplina>> buscarTodasClassesDisciplines() {
		List<ClassSt> classSt = classStService.buscarTodasClasses();

		List<ClassDTODisciplina> classDTos = classSt.stream().map(turma -> {

			List<DisciplineTurmaDTO> disciplinas = turma.getDisciplinaTurmas().stream()
					.map(disciplina -> new DisciplineTurmaDTO(disciplina.getId(), disciplina.getNomeDisciplina()))
					.collect(Collectors.toList());

			return new ClassDTODisciplina(turma.getNomeTurma(), turma.getPeriodoTurma(),
					turma.getDisciplinaTurmas().stream().map(Discipline::getId).collect(Collectors.toList()),
					disciplinas);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(classDTos);
	}

	/**
	 * Retorna a lista de alunos de uma turma específica.
	 * 
	 * - Recebe o ID de uma turma. - Busca a turma e converte os alunos para o
	 * formato de resposta. - Se a turma não for encontrada, retorna erro HTTP 404.
	 * 
	 * @param id ID da turma.
	 * @return Resposta com a lista de alunos da turma.
	 */
	@GetMapping("/class/students/{id}")
	public ResponseEntity<?> buscarClasseStudentsUnica(@PathVariable Long id) {
		try {
			ClassSt buscaClasse = classStService.buscarClasseUnica(id);
			if (buscaClasse != null) {
				List<StudentDTO> students = buscaClasse.getStudents().stream().map(student -> {

					List<NoteDTO> notas = student.getNotas().stream().map(nota -> {
						Discipline disciplina = nota.getDisciplineId();
						return new NoteDTO(disciplina.getNomeDisciplina(), nota.getNota());
					}).collect(Collectors.toList());

					return new StudentDTO(student.getNomeAluno(), student.getDataNascimentoAluno().toString(), notas,
							student.getId(), student.getIdentifierCode());
				}).collect(Collectors.toList());

				ClassDTOTwo classDTO = new ClassDTOTwo(buscaClasse.getNomeTurma(), buscaClasse.getPeriodoTurma(),
						buscaClasse.getId(), students);
				return ResponseEntity.ok(classDTO);
			}
			return new ResponseEntity<>(Map.of("error", "Classe não encontrada"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar alunos da classe: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Retorna a lista de professores e disciplinas de uma turma específica.
	 * 
	 * - Recebe o ID de uma turma. - Busca a turma e converte os professores e
	 * disciplinas para o formato de resposta. - Se a turma não for encontrada,
	 * retorna erro HTTP 404.
	 * 
	 * @param id ID da turma.
	 * @return Resposta com os professores e disciplinas da turma.
	 */
	@GetMapping("/class/teacher/disciplinas/{id}")
	public ResponseEntity<?> buscarClasseTeachersUnica(@PathVariable Long id) {
		try {
			ClassSt buscaClasse = classStService.buscarClasseUnica(id);
			if (buscaClasse != null) {
				List<TeacherTurmaDTO> teachers = buscaClasse.getClasses().stream()
						.map(teacher -> new TeacherTurmaDTO(teacher.getId(), teacher.getNomeDocente()))
						.collect(Collectors.toList());
				List<DisciplineTurmaDTO> disciplinas = buscaClasse.getDisciplinaTurmas().stream()
						.map(disciplina -> new DisciplineTurmaDTO(disciplina.getId(), disciplina.getNomeDisciplina()))
						.collect(Collectors.toList());
				ClassDisciplinasTeacherDTO classDTO = new ClassDisciplinasTeacherDTO(buscaClasse.getId(),
						buscaClasse.getNomeTurma(), buscaClasse.getAnoLetivoTurma(), buscaClasse.getPeriodoTurma(),
						buscaClasse.getCapacidadeMaximaTurma(), buscaClasse.getSalaTurma(),
						buscaClasse.getStudents().size(), teachers, disciplinas);
				return ResponseEntity.ok(classDTO);
			}
			return new ResponseEntity<>(Map.of("error", "Classe não encontrada"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar professores da classe: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Atualiza os dados de uma turma existente.
	 * 
	 * - Recebe o ID da turma e os novos dados. - Se a turma existir, atualiza as
	 * informações e retorna a turma atualizada. - Se a turma não for encontrada,
	 * retorna erro HTTP 404.
	 * 
	 * @param id      ID da turma a ser atualizada.
	 * @param classSt Objeto com os novos dados da turma.
	 * @return Resposta com a turma atualizada ou erro.
	 */
	@PutMapping("/class/{id}")
	public ResponseEntity<?> atualizarClasse(@PathVariable Long id, @RequestBody ClassUpdateDTO classUpdate) {
		try {
			List<Teacher> teacher = teacherRepo.findAllById(classUpdate.idTeacher());
			List<Discipline> disciplines = disRepo.findAllById(classUpdate.disciplineId());
			validarClassStUpdate(classUpdate);

			if (teacher.size() != classUpdate.idTeacher().size()) {
				return new ResponseEntity<>(Map.of("error", "Professor não encontrado."), HttpStatus.BAD_REQUEST);
			}
			if (disciplines.size() != classUpdate.disciplineId().size()) {
				return new ResponseEntity<>(Map.of("error", "Disciplina não encontrada."), HttpStatus.BAD_REQUEST);
			}

			ClassSt classSt = new ClassSt();
			classSt.setNomeTurma(classUpdate.nomeTurma);
			classSt.setAnoLetivoTurma(classUpdate.anoLetivoTurma);
			classSt.setPeriodoTurma(classUpdate.periodoTurma);
			classSt.setCapacidadeMaximaTurma(classUpdate.capacidadeMaximaTurma);
			classSt.setSalaTurma(classUpdate.salaTurma);

			classSt.setDisciplinaTurmas(disciplines);
			classSt.setClasses(teacher);

			ClassSt atualizado = classStService.atualizarClasse(id, classSt);

			return new ResponseEntity<>(atualizado, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao atualizar classe: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Deleta uma turma existente.
	 * 
	 * - Recebe o ID da turma a ser deletada. - Se a turma existir, a deleta do
	 * sistema. - Se a turma não for encontrada, retorna erro HTTP 404.
	 * 
	 * @param id ID da turma a ser deletada.
	 * @return Resposta indicando sucesso ou erro.
	 */
	@DeleteMapping("/class/{id}")
	@Transactional
	public ResponseEntity<?> deletarClasse(@PathVariable Long id) {
		try {
			classStService.deletarClasse(id);
			return ResponseEntity.noContent().build(); // Retorna 204 No Content
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", "Erro ao deletar classe: " + e.getMessage()));
		}
	}

}
