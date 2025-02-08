package com.onAcademy.tcc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onAcademy.tcc.dto.LoginDTO;
import com.onAcademy.tcc.model.Institution;
import com.onAcademy.tcc.service.InstitutionService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Institution", description = "EndPoint de instituição")
@RestController
@RequestMapping("/api")
public class InstitutionController {
	@Autowired
	private InstitutionService institutionService;
	
	@PostMapping("/institution/login")
	public ResponseEntity<String> loginInstituicao(@RequestBody LoginDTO loginDTO){
		String token = institutionService.loginInstituicao(loginDTO.cnpjInstitution(), loginDTO.senhaInstitution());
		return new ResponseEntity<>(token, HttpStatus.OK);
	}
	
	@PostMapping("/institution")
	public ResponseEntity<Institution> criarInstituicao(@RequestBody Institution institution){
		Institution institution1 = institutionService.criarInstituicao(institution);
		return new ResponseEntity<>(institution1, HttpStatus.CREATED);
	}
	
	@GetMapping("/institution")
	public ResponseEntity<List<Institution>> buscarInstituicao(){
		List<Institution> institution = institutionService.buscarInstituicao();
		return new ResponseEntity<>(institution, HttpStatus.OK);
	}
	
	@PutMapping("/institution/{id}")
	public ResponseEntity<Institution> atualizarInstituicao(@PathVariable Long id, @RequestBody Institution institution){
	 	Institution atualizarInstituicao = institutionService.atualizarInstituicao(id, institution);
	 	if(atualizarInstituicao != null) {
	 		return new ResponseEntity<>(atualizarInstituicao, HttpStatus.OK);
	 	}
	 	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
