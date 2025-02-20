package com.onAcademy.tcc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> loginInstituicao(@RequestBody LoginDTO loginDTO) {
        try {
            String token = institutionService.loginInstituicao(loginDTO.identifierCode(), loginDTO.password());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Falha no login: " + e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/institution")
    public ResponseEntity<?> criarInstituicao(@RequestBody Institution institution) {
        try {
            Institution institution1 = institutionService.criarInstituicao(institution);
            return new ResponseEntity<>(institution1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao criar instituição: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/institution")
    public ResponseEntity<?> buscarInstituicao() {
        try {
            List<Institution> institutions = institutionService.buscarInstituicao();
            return new ResponseEntity<>(institutions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao buscar instituições: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/institution/{id}")
    public ResponseEntity<?> atualizarInstituicao(@PathVariable Long id, @RequestBody Institution institution) {
        try {
            Institution atualizarInstituicao = institutionService.atualizarInstituicao(id, institution);
            if (atualizarInstituicao != null) {
                return new ResponseEntity<>(atualizarInstituicao, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Map.of("error", "Instituição não encontrada"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao atualizar instituição: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
