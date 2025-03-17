package com.onAcademy.tcc.dto;

/**
 * Classe usada para transportar dados de uma "Class" (como nome e ID) 
 * entre diferentes partes do sistema, como do banco de dados para a interface do usuário.
 *
 * @param nome Nome da classe.
 * @param id   Número único que identifica a classe.
 */

public record ClassDTO(String nome, Long id) {
    // O uso de 'record' já garante a imutabilidade e a geração automática
    // de métodos como equals(), hashCode(), toString(), getters, etc.
}