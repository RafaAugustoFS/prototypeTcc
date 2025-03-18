package com.onAcademy.tcc.dto;

/**
 * Classe DTO (Data Transfer Object) que representa os dados de login de um
 * professor. Este objeto é utilizado para transferir as informações necessárias
 * para autenticar um professor na aplicação, como o código identificador e a
 * senha.
 * 
 * @param identifierCode O código identificador único do professor.
 * @param password       A senha do professor para autenticação.
 */

public record LoginTeacherDTO(String identifierCode, String password) {

}
