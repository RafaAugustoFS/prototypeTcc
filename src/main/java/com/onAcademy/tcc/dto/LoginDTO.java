package com.onAcademy.tcc.dto;
/**
 * Classe DTO (Data Transfer Object) que representa os dados de login de um usuário.
 * Este objeto é utilizado para transferir as informações necessárias para autenticar 
 * um usuário na aplicação, como o código identificador e a senha.
 * @param identifierCode O código identificador único do usuário.
 * @param password A senha do usuário para autenticação.
 */
public record LoginDTO(String identifierCode, String password) {
	
}
