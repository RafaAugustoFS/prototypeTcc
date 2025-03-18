package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Event;

/**
 * Repositório para a entidade "Event".
 * 
 * - Estende a interface JpaRepository, fornecendo os métodos básicos de
 * persistência (salvar, buscar, atualizar, excluir) para a entidade `Event`. -
 * A interface `EventRepo` não requer implementação explícita, pois o Spring
 * Data JPA fornece a implementação automaticamente em tempo de execução.
 * 
 * A interface oferece os seguintes recursos: - **Crud básico**: Métodos como
 * `save()`, `findById()`, `findAll()`, `deleteById()` estão disponíveis por
 * herança da `JpaRepository`.
 * 
 * A principal finalidade desta interface é permitir o acesso à tabela que
 * armazena os dados dos eventos, permitindo a execução de operações de CRUD
 * (Create, Read, Update, Delete) de forma simplificada.
 * 
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.onAcademy.tcc.model.Event
 */

public interface EventRepo extends JpaRepository<Event, Long> {

}
