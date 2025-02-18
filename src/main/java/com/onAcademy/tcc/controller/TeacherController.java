package com.onAcademy.tcc.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onAcademy.tcc.dto.LoginTeacherDTO;
import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.repository.ClassStRepo;
import com.onAcademy.tcc.repository.DisciplineRepo;
import com.onAcademy.tcc.repository.TeacherRepo;
import com.onAcademy.tcc.service.TeacherService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Teacher", description = "EndPoint de professor")
@RestController
@RequestMapping("/api")
public class TeacherController {
	record ClassTeacherDTO(String nomeTurma, Long turmaId) {
	}

	record DisciplineDTO(String nomeDisiciplina, Long discipline_id) {
	}

	record TeacherDTO(String nomeDocente, List<Long> turmaId, List<ClassTeacherDTO> classTeacherDTO,
			List<Long> disciplineId, List<DisciplineDTO> DisciplineDTO) {
	}

	record TeacherDTOTwo(String nomeDocente, Long id, List<ClassTeacherDTO> classes) {
	}

	@Autowired
	private DisciplineRepo disciplineRepo;

	@Autowired
	private ClassStRepo classStRepo;

	@Autowired
	private TeacherRepo teacherRepo;

	@Autowired
	private TeacherService teacherService;

	@PostMapping("/teacher/login")
	public ResponseEntity<Map<String, String>> loginTeacher(@RequestBody LoginTeacherDTO loginTeacherDTO) {
		String token = teacherService.loginTeacher(loginTeacherDTO.identifierCode(), loginTeacherDTO.password());

		Map<String, String> response = new HashMap<>();
		response.put("token", token);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/teacher")
	@PreAuthorize("hasRole('INSTITUTION')")
	public ResponseEntity<Teacher> criarTeacher(@RequestBody TeacherDTO teacherDTO) {

		List<ClassSt> classSt = classStRepo.findAllById(teacherDTO.turmaId());
		if (classSt.size() != teacherDTO.turmaId().size()) {
			throw new RuntimeException("Algumas turmas não foram adicionadas");
		}

		List<Discipline> disciplines = disciplineRepo.findAllById(teacherDTO.disciplineId());
		if (disciplines.size() != teacherDTO.disciplineId().size()) {
			throw new RuntimeException("Algumas disciplinas não foram adicionadas");
		}
		Teacher teacher = new Teacher();
		teacher.setNomeDocente(teacherDTO.nomeDocente());

		if (teacher.getClasses() == null) {
			teacher.setClasses(new ArrayList<>());
		}
		teacher.getClasses().addAll(classSt);

		if (teacher.getDisciplines() == null) {
			teacher.setDisciplines(new ArrayList<>());
		}
		teacher.getDisciplines().addAll(disciplines);
		teacherRepo.save(teacher);
		return new ResponseEntity<>(teacher, HttpStatus.CREATED);
	}

	@GetMapping("/teacher")
	public ResponseEntity<List<Teacher>> buscarTeachers() {
		List<Teacher> teachers = teacherService.buscarTeachers();
		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}

	@PutMapping("/teacher/{id}")
	public ResponseEntity<Teacher> editarTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
		Teacher atualizarTeachers = teacherService.atualizarTeacher(id, teacher);

		if (atualizarTeachers != null) {
			return new ResponseEntity<>(atualizarTeachers, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/teacher/{id}")
	public ResponseEntity<TeacherDTOTwo> buscarTeacherUnico(@PathVariable Long id) {
		Teacher buscarUnico = teacherService.buscarUnicoTeacher(id);

		if (buscarUnico != null) {
			List<ClassTeacherDTO> classes = buscarUnico.getClasses().stream()
					.map(classe -> new ClassTeacherDTO(classe.getNomeTurma(), classe.getId()))
					.collect(Collectors.toList());
			TeacherDTOTwo teacherDTOTwo = new TeacherDTOTwo(buscarUnico.getNomeDocente(), buscarUnico.getId(), classes);
			return ResponseEntity.ok(teacherDTOTwo);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@DeleteMapping("/teacher/{id}")
	public ResponseEntity<Teacher> deletarTeacher(@PathVariable Long id) {
		Teacher deletarTeacher = teacherService.deletarTeacher(id);
		return new ResponseEntity<>(deletarTeacher, HttpStatus.OK);
	}

}
