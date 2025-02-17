package com.onAcademy.tcc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onAcademy.tcc.controller.StudentController.ClassDTO;
import com.onAcademy.tcc.controller.StudentController.NoteDTO;
import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.repository.ClassStRepo;
import com.onAcademy.tcc.repository.DisciplineRepo;
import com.onAcademy.tcc.service.DisciplineService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Discipline", description = "EndPoint de disciplina")
@RestController
@RequestMapping("/api")
public class DisciplineController {

	@Autowired
	private DisciplineService disciplineService;

	@Autowired
	private ClassStRepo classStRepo;

	@Autowired
	private DisciplineRepo disciplineRepository;

	record TurmaDisciplinaDTO(String nomeTurma, Long idTurma) {
	}

	record DisciplineDTO(String nomeDisciplina, Long turmaId, List<TurmaDisciplinaDTO> turmas) {
	}

	@PostMapping("/discipline")
	public ResponseEntity<Discipline> criarDiscipline(@RequestBody DisciplineDTO disciplineDTO) {
		ClassSt classSt = classStRepo.findById(disciplineDTO.turmaId())
				.orElseThrow(() -> new RuntimeException("Turma não encontrada"));

		Discipline discipline = new Discipline();
		discipline.setNomeDisciplina(disciplineDTO.nomeDisciplina());

		if (discipline.getTurmaDisciplinas() == null) {
			discipline.setTurmaDisciplinas(new ArrayList<>());
		}

		discipline.getTurmaDisciplinas().add(classSt);

		disciplineRepository.save(discipline);
		return new ResponseEntity<>(discipline, HttpStatus.CREATED);
	}

	@GetMapping("/discipline")
	public ResponseEntity<List<Discipline>> buscarDisciplines() {
		List<Discipline> buscarDisciplines = disciplineService.buscarDisciplines();
		return new ResponseEntity<>(buscarDisciplines, HttpStatus.OK);
	}

	@PutMapping("/discipline/{id}")
	public ResponseEntity<Discipline> editarDiscipline(@PathVariable Long id, @RequestBody Discipline discipline) {
		Discipline editarDiscipline = disciplineService.atualizarDiscipline(id, discipline);
		if (editarDiscipline != null) {
			return new ResponseEntity<>(editarDiscipline, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/discipline/{id}")
	public ResponseEntity<DisciplineDTO> buscarUnica(@PathVariable Long id) {
		Discipline buscarUnica = disciplineService.buscarUnicaDisciplina(id);
		if (buscarUnica != null) {

			List<TurmaDisciplinaDTO> turmas = buscarUnica.getTurmaDisciplinas().stream()
					.map(turma -> new TurmaDisciplinaDTO(turma.getNomeTurma(), turma.getId()))
					.collect(Collectors.toList());

			DisciplineDTO disciplineDTO = new DisciplineDTO(buscarUnica.getNomeDisciplina(),
					buscarUnica.getTurmaDisciplinas().get(0).getId(), turmas);

			return new ResponseEntity<>(disciplineDTO, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/discipline/{id}")
	public ResponseEntity<Discipline> deletar(@PathVariable Long id) {
		Discipline deletar = disciplineService.deleteDiscipline(id);
		return new ResponseEntity<>(deletar, HttpStatus.OK);
	}
}
