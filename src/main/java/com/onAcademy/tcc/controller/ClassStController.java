package com.onAcademy.tcc.controller;

import java.util.ArrayList;
import java.util.List;
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

import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.repository.ClassStRepo;
import com.onAcademy.tcc.repository.DisciplineRepo;
import com.onAcademy.tcc.service.ClassStService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Class", description = "EndPoint de turma")
@RestController
@RequestMapping("/api")
public class ClassStController {
	@Autowired
	private ClassStService classStService;

	@Autowired
	private ClassStRepo classStRepo;

	@Autowired
	private DisciplineRepo disciplineRepo;

	record DisciplineTurma(String nomeDiscipline, Long id) {};
	
	record StudentDTO(String nomeAluno, String dataNascimentoAluno, Long id) {
	};

	record ClassDTO(String nomeTurma, String periodoTurma, List<Long> disciplineId) {
	};
	
	record ClassDTO2(String nomeTurma, String periodoTurma, List<String> disciplineNames) {
	};
	
	record ClassDTOTwo(String nomeTurma, String periodoTurma, Long id, List<StudentDTO> students) {
	};

	@PostMapping("/class")
	public ResponseEntity<ClassSt> criarClasse(@RequestBody ClassDTO classDTO) {
	    List<Discipline> disciplines = disciplineRepo.findAllById(classDTO.disciplineId());
	    
	    
	    if (disciplines.size() != classDTO.disciplineId().size()) {
	        throw new RuntimeException("Algumas disciplinas não foram encontradas.");
	    }
	    
	  
	    ClassSt classSt = new ClassSt();
	    classSt.setNomeTurma(classDTO.nomeTurma);
	    classSt.setPeriodoTurma(classDTO.periodoTurma);
	    
	    
	    if (classSt.getDisciplinaTurmas() == null) {
	        classSt.setDisciplinaTurmas(new ArrayList<>());
	    }

	    
	    classSt.getDisciplinaTurmas().addAll(disciplines);
	
	    classStRepo.save(classSt);
	    
	    return new ResponseEntity<>(classSt, HttpStatus.CREATED);
	}
	@GetMapping("/class/discipline")
	public ResponseEntity<List<ClassDTO>> buscarTodasClassesDisciplines() {
		List<ClassSt> classSt = classStService.buscarTodasClasses();

		List<ClassDTO> classDTos = classSt.stream().map(turma -> {
	        List<Long> disciplineIds = turma.getDisciplinaTurmas().stream()
	            .map(disciplina -> disciplina.getId()) 
	            .collect(Collectors.toList()); 

	       
	        return new ClassDTO(turma.getNomeTurma(), turma.getPeriodoTurma(), disciplineIds);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(classDTos);

	}
	
	@GetMapping("/class")
	public ResponseEntity<List<ClassSt>> buscarTodasClasses() {
		List<ClassSt> classSt = classStService.buscarTodasClasses();
		return new ResponseEntity<>(classSt, HttpStatus.OK);
	}

	@GetMapping("/class/{id}")
	public ResponseEntity<ClassDTOTwo> buscarClasseUnica(@PathVariable Long id) {
		ClassSt buscaClasse = classStService.buscarClasseUnica(id);
		if (buscaClasse != null) {
			List<StudentDTO> students = buscaClasse.getStudents().stream()
					.map(student -> new StudentDTO(student.getNomeAluno(), student.getDataNascimentoAluno().toString(),
							student.getId()))
					.collect(Collectors.toList());
			ClassDTOTwo classDTO = new ClassDTOTwo(buscaClasse.getNomeTurma(), buscaClasse.getPeriodoTurma(),
					buscaClasse.getId(), students);
			return ResponseEntity.ok(classDTO);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/class/{id}")
	public ResponseEntity<ClassSt> atualizarClasse(@PathVariable Long id, @RequestBody ClassSt classSt) {
		ClassSt atualizarClasse = classStService.atualizarClasse(id, classSt);
		if (atualizarClasse != null) {
			return new ResponseEntity<>(atualizarClasse, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/class/{id}")
	public ResponseEntity<ClassSt> deletarClasse(@PathVariable Long id) {
		ClassSt deletarClasse = classStService.deletarClasse(id);
		if (deletarClasse != null) {
			return new ResponseEntity<>(deletarClasse, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
