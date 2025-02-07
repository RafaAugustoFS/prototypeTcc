package com.onAcademy.tcc.controller;

import java.util.List;

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

import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.service.DisciplineService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Discipline", description = "EndPoint de disciplina")
@RestController
@RequestMapping("/api")
public class DisciplineController {

	@Autowired
	private DisciplineService disciplineService;
	
	@PostMapping("/discipline")
	public ResponseEntity<Discipline> criarDiscipline(@RequestBody Discipline discipline){
		Discipline criarDiscipline = disciplineService.criarDiscipline(discipline);
		return new ResponseEntity<>(criarDiscipline, HttpStatus.CREATED);
		
	}
	
	@GetMapping("/discipline")
	public ResponseEntity<List<Discipline>> buscarDisciplines(){
		List<Discipline> buscarDisciplines = disciplineService.buscarDisciplines();
		return new ResponseEntity<>(buscarDisciplines, HttpStatus.OK);
	}
	
	@PutMapping("/discipline/{id}")
	public ResponseEntity<Discipline> editarDiscipline(@PathVariable Long id, @RequestBody Discipline discipline){
		Discipline editarDiscipline = disciplineService.atualizarDiscipline(id, discipline);
		if(editarDiscipline != null) {
			return new ResponseEntity<>(editarDiscipline, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	@GetMapping("/discipline/{id}")
	public ResponseEntity<Discipline> buscarUnica(@PathVariable Long id){
		Discipline buscarUnica = disciplineService.buscarUnicaDisciplina(id);
		return new ResponseEntity<>(buscarUnica, HttpStatus.OK);

	}
	
	@DeleteMapping("/discipline/{id}")
	public ResponseEntity<Discipline> deletar(@PathVariable Long id){
		Discipline deletar = disciplineService.deleteDiscipline(id);
		return new ResponseEntity<>(deletar, HttpStatus.OK);
	}
}
