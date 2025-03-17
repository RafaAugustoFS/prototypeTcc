package com.onAcademy.tcc.dto;

/**
 * @param identifierCode Código de identificação do estudante (matrícula).
 * @param password       Senha do estudante.
 */
public record LoginStudent(String identifierCode, String password) {
}