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

	/**
	 * Realiza o login de uma instituição.
	 * 
	 * - Recebe os dados de login da instituição (código de identificação e senha).
	 * - Se os dados estiverem corretos, retorna um token JWT para autenticação
	 * futura. - Caso contrário, retorna uma resposta de erro HTTP 401
	 * (Unauthorized).
	 * 
	 * @param loginDTO Objeto contendo o código de identificação e senha da
	 *                 instituição.
	 * @return Resposta HTTP com o token gerado ou erro de autenticação.
	 */
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

	/**
	 * Cria uma nova instituição.
	 * 
	 * - Recebe os dados da instituição via requisição HTTP POST. - Se os dados
	 * forem válidos, cria a instituição no banco de dados. - Caso contrário,
	 * retorna uma resposta de erro HTTP 400 (Bad Request).
	 * 
	 * @param institution Objeto contendo os dados da instituição a ser criada.
	 * @return Resposta HTTP com a instituição criada ou erro.
	 */
	@PostMapping("/institution")
	public ResponseEntity<?> criarInstituicao(@RequestBody Institution institution) {
		try {
			Institution institution1 = institutionService.criarInstituicao(institution);
			return new ResponseEntity<>(institution1, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao criar instituição: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Busca todas as instituições cadastradas.
	 * 
	 * - Retorna a lista de todas as instituições registradas no sistema. - Caso
	 * ocorra algum erro na consulta, retorna uma resposta de erro HTTP 500
	 * (Internal Server Error).
	 * 
	 * @return Resposta HTTP com a lista de instituições ou erro.
	 */
	@GetMapping("/institution")
	public ResponseEntity<?> buscarInstituicao() {
		try {
			List<Institution> institutions = institutionService.buscarInstituicao();
			return new ResponseEntity<>(institutions, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Map.of("error", "Erro ao buscar instituições: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Atualiza as informações de uma instituição existente.
	 * 
	 * - Recebe o ID da instituição e os dados atualizados via requisição HTTP PUT.
	 * - Se a instituição for encontrada, atualiza os dados e retorna a instituição
	 * atualizada. - Caso a instituição não seja encontrada, retorna uma resposta de
	 * erro HTTP 404 (Not Found).
	 * 
	 * @param id          ID da instituição a ser atualizada.
	 * @param institution Objeto contendo os novos dados da instituição.
	 * @return Resposta HTTP com a instituição atualizada ou erro.
	 */
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
			return new ResponseEntity<>(Map.of("error", "Erro ao atualizar instituição: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}
}
