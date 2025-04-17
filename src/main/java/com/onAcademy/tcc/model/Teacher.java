package com.onAcademy.tcc.model;

import java.util.Date;
import java.util.List;
import java.util.Random;

import com.onAcademy.tcc.dto.TeacherDTO;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostPersist;
import lombok.Data;

/**
 * Representa um professor no sistema.
 * Esta entidade armazena informações pessoais do professor, como nome, data de nascimento,
 * e-mail, telefone, código de identificação, senha, URL da imagem, e relacionamentos com
 * feedbacks, disciplinas, turmas, formulários de feedback e lembretes.
 *
 * @param id                      Identificador único do professor.
 * @param nomeDocente             Nome do professor.
 * @param dataNascimentoDocente   Data de nascimento do professor.
 * @param emailDocente            E-mail do professor.
 * @param telefoneDocente         Telefone do professor.
 * @param identifierCode          Código de identificação único gerado automaticamente.
 * @param password                Senha do professor, gerada automaticamente com base no nome e ano de nascimento.
 * @param imageUrl                URL da imagem do professor.
 * @param feedback                Lista de feedbacks recebidos dos estudantes.
 * @param disciplines             Lista de disciplinas lecionadas pelo professor.
 * @param teachers                Lista de turmas associadas ao professor.
 * @param feedbackProfessor       Lista de feedbacks enviados pelo professor.
 * @param feedbackForm            Lista de formulários de feedback criados pelo professor.
 * @param reminder                Lista de lembretes criados pelo professor.
 */
@Entity
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeDocente;
    private Date dataNascimentoDocente;
    private String emailDocente;
    private String telefoneDocente;
    private String identifierCode;
    private String password;
    private String imageUrl;

    public static final String ENROLLMENT_PREFIX = "p";
    private static final int IDENTIFIER_CODE_LENGTH = 10;

    @OneToMany(mappedBy = "recipientTeacher")
    private List<FeedBackByStudent> feedback;

    @ManyToMany
    @JoinTable(
        name = "teacher_discipline",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    private List<Discipline> disciplines;

    @ManyToMany(mappedBy = "classes")
    private List<ClassSt> teachers;

    @OneToMany(mappedBy = "createdBy")
    private List<FeedbackByTeacher> feedbackProfessor;

    @OneToMany(mappedBy = "createdBy")
    private List<FeedbackForm> feedbackForm;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reminder> reminder;

    /**
     * Gera uma senha aleatória para o professor com base no nome e ano de nascimento.
     *
     * @param teacherDTO O professor para o qual a senha será gerada.
     * @return A senha gerada.
     */
    public static String generateRandomPassword(TeacherDTO teacherDTO) {
        String year = String.valueOf(teacherDTO.getDataNascimentoDocente().getYear());
        return ENROLLMENT_PREFIX + year + teacherDTO.getNomeDocente().replaceAll("\\s", "").toLowerCase();

    }

    /**
     * Gera um código de identificação único para o professor após a persistência.
     * O código é composto por 10 dígitos aleatórios.
     */
    @PostPersist
    private void generateIdentifierCode() {
        String numbers = "0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        sb.append(ENROLLMENT_PREFIX);
        for (int i = 0; i < IDENTIFIER_CODE_LENGTH; i++) {
            sb.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        this.identifierCode = sb.toString();
    }
}