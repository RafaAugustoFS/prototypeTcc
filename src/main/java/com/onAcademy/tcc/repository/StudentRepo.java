package com.onAcademy.tcc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Student;

/**
 * Repositório para a entidade {@link Student}.
 * Esta interface estende {@link JpaRepository}, fornecendo métodos CRUD (Create, Read, Update, Delete)
 * e operações de paginação e ordenação para a entidade {@link Student}.
 * Além disso, inclui métodos personalizados para buscar um estudante pelo código de identificação
 * e verificar a existência de um estudante com base no e-mail ou telefone.
 *
 * @see JpaRepository
 * @see Student
 */
public interface StudentRepo extends JpaRepository<Student, Long> {

    /**
     * Busca um estudante pelo código de identificação (matrícula).
     *
     * @param registration Código de identificação do estudante.
     * @return Um {@link Optional} contendo o estudante, se encontrado.
     */
    Optional<Student> findByIdentifierCode(String registration);

    /**
     * Verifica se já existe um estudante com o e-mail fornecido.
     *
     * @param emailAluno E-mail do estudante.
     * @return `true` se o e-mail já estiver em uso, `false` caso contrário.
     */
    boolean existsByEmailAluno(String emailAluno);

    /**
     * Verifica se já existe um estudante com o telefone fornecido.
     *
     * @param telefoneAluno Telefone do estudante.
     * @return `true` se o telefone já estiver em uso, `false` caso contrário.
     */
    boolean existsByTelefoneAluno(String telefoneAluno);
    
    List<Student> findByTurmaId(Long turmaId);

	Optional<Student> findByEmailAluno(String emailAluno);

	Optional<Student> findByTelefoneAluno(String telefoneAluno);
}