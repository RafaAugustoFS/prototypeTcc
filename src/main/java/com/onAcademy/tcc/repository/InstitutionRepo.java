package com.onAcademy.tcc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Institution;

/**
 * Repositório para a entidade "Institution".
 * 
 * - Estende a interface `JpaRepository`, fornecendo os métodos básicos de
 * persistência (salvar, buscar, atualizar, excluir) para a entidade
 * `Institution`. - A interface `InstitutionRepo` inclui um método adicional,
 * `findByidentifierCode`, que permite buscar uma instituição com base no código
 * de identificação único (`identifierCode`), utilizado para realizar o login na
 * aplicação.
 * 
 * A interface oferece os seguintes recursos: - **Crud básico**: Métodos como
 * `save()`, `findById()`, `findAll()`, `deleteById()` estão disponíveis por
 * herança da `JpaRepository`. - **Busca personalizada**: O método
 * `findByidentifierCode` permite buscar uma instituição pelo código de
 * identificação único, usado no processo de autenticação de usuários.
 * 
 * A principal finalidade desta interface é fornecer um mecanismo de acesso à
 * tabela que armazena os dados das instituições, permitindo a execução de
 * operações de CRUD e consultas personalizadas, além de apoiar a autenticação
 * de usuários na aplicação.
 * 
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.onAcademy.tcc.model.Institution
 */

public interface InstitutionRepo extends JpaRepository<Institution, Long> {
	Optional<Institution> findByidentifierCode(String idetifierCode);
}
