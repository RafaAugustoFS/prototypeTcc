package com.onAcademy.tcc.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.onAcademy.tcc.model.Teacher;

/**
 * Repositório para a entidade {@link Teacher}.
 * Esta interface estende {@link JpaRepository}, fornecendo métodos CRUD (Create, Read, Update, Delete)
 * e operações de paginação e ordenação para a entidade {@link Teacher}.
 * Além disso, inclui métodos personalizados para buscar um professor pelo código de identificação
 * e verificar a existência de um professor com base no e-mail ou telefone.
 *
 * @see JpaRepository
 * @see Teacher
 */
public interface TeacherRepo extends JpaRepository<Teacher, Long> {

    /**
     * Busca um professor pelo código de identificação.
     *
     * @param identifierCode Código de identificação do professor.
     * @return Um {@link Optional} contendo o professor, se encontrado.
     */
    Optional<Teacher> findByIdentifierCode(String identifierCode);

    /**
     * Verifica se já existe um professor com o e-mail fornecido.
     *
     * @param emailDocente E-mail do professor.
     * @return `true` se o e-mail já estiver em uso, `false` caso contrário.
     */
    boolean existsByEmailDocente(String emailDocente);

    /**
     * Verifica se já existe um professor com o telefone fornecido.
     *
     * @param telefoneDocente Telefone do professor.
     * @return `true` se o telefone já estiver em uso, `false` caso contrário.
     */
    boolean existsByTelefoneDocente(String telefoneDocente);
}