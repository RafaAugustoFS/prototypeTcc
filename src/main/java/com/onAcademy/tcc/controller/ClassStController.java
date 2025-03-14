package com.onAcademy.tcc.controller;

import java.sql.Date;
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

	record ClassDTO(String nomeTurma, Date anoLetivoTurma, int capacidadeMaximaTurma, int salaTurma,
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

	record ClassDisciplinasTeacherDTO(Long id, String nomeTurma, Date anoLetivoTurma, String periodoTurma,
			int capacidadeMaximaTurma, int salaTurma, int quantidadeAlunos, List<TeacherTurmaDTO> teachers,
			List<DisciplineTurmaDTO> disciplines) {
	}

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
			if(disciplines.size() != classDTO.disciplineId().size()) {
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

	public void validarClassSt(ClassDTO classDTO) {
		if (classDTO.nomeTurma.isEmpty()) {
			throw new IllegalArgumentException("Nome da turma é obrigatório.");
		}
		if (classDTO.anoLetivoTurma == null) {
			throw new IllegalArgumentException("Ano letivo da turma é obrigatório.");
		}
		if (classDTO.periodoTurma.isEmpty()) {
			throw new IllegalArgumentException("Periodo da turma é obrigatório.");
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

	
	}

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

	@PutMapping("/class/{id}")
	public ResponseEntity<?> atualizarClasse(@PathVariable Long id, @RequestBody ClassSt classSt) {
		try {
			ClassSt atualizarClasse = classStService.atualizarClasse(id, classSt);
			if (atualizarClasse != null) {
				return new ResponseEntity<>(atualizarClasse, HttpStatus.OK);
			}
			return new ResponseEntity<>(Map.of("error", "Classe não encontrada"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao atualizar classe: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

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
