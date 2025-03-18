package com.onAcademy.tcc.dto;

/**
 * Classe DTO (Data Transfer Object) que representa o feedback de um estudante.
 * Este objeto contém informações básicas sobre um estudante para ser
 * transferido entre as camadas da aplicação, como a camada de serviços ou de
 * controladores.
 * 
 * @param id        O identificador único do estudante.
 * @param nomeAluno O nome do estudante.
 */

public record FeedbackStudentDTO(Long id, String nomeAluno) {

}
