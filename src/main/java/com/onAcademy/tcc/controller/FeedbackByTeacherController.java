package com.onAcademy.tcc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onAcademy.tcc.model.FeedbackByTeacher;
import com.onAcademy.tcc.service.FeedbackByTeacherService;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller responsável por gerenciar operações relacionadas a feedbacks de professores.
 */
@Tag(name = "Feedback teacher", description = "EndPoint de feedback do professor")
@RestController
@RequestMapping("/api")
public class FeedbackByTeacherController {

    @Autowired
    private FeedbackByTeacherService feedbackByTeacherService;

    /**
     * DTO para representar um aluno de forma simplificada.
     */
    record StudentDTO(String nomeAluno, Long id) {
    }

    /**
     * DTO para representar uma turma de forma simplificada.
     */
    record ClassStDTO(String nomeTurma, Long id) {
    }

    /**
     * DTO para representar o professor que criou o feedback.
     */
    record CreatedByDTO(String nomeDocente, Long id) {
    }

    /**
     * DTO para representar um feedback direcionado a um aluno.
     */
    record FeedbackDTO(String conteudo, CreatedByDTO createdBy, StudentDTO student) {
    }

    /**
     * DTO para representar um feedback direcionado a uma turma.
     */
    record Feedback2DTO(String conteudo, CreatedByDTO createdBy, ClassStDTO classSt) {
    }

    /**
     * Cria um novo feedback.
     *
     * @param feedback Objeto contendo os dados do feedback.
     * @return ResponseEntity com o feedback criado ou uma mensagem de erro.
     */
    @PostMapping("/feedbackTeacher")
    public ResponseEntity<?> criarFeedback(@RequestBody FeedbackByTeacher feedback) {
        try {
            validarFeedback(feedback);
            FeedbackByTeacher feedbackCriado = feedbackByTeacherService.criarFeedback(feedback);
            return new ResponseEntity<>(feedbackCriado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao criar feedback: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Valida os dados de um feedback.
     *
     * @param feedback Feedback a ser validado.
     * @throws IllegalArgumentException Se o conteúdo do feedback estiver vazio.
     */
    private void validarFeedback(FeedbackByTeacher feedback) {
        if (feedback.getConteudo() == null || feedback.getConteudo().trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor, preencha o conteúdo do feedback.");
        }
    }

    /**
     * Retorna uma lista de todos os feedbacks.
     *
     * @return ResponseEntity com a lista de feedbacks ou uma mensagem de erro.
     */
    @GetMapping("/feedbackTeacher")
    public ResponseEntity<?> buscarTodosFeedbacks() {
        try {
            List<FeedbackByTeacher> feedbacks = feedbackByTeacherService.buscarTodosFeedbacks();
            return new ResponseEntity<>(feedbacks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao buscar feedbacks: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca um feedback direcionado a um aluno pelo ID.
     *
     * @param id ID do feedback a ser buscado.
     * @return ResponseEntity com o feedback no formato DTO ou uma mensagem de erro.
     */
    @GetMapping("/feedbackTeacherStudent/{id}")
    public ResponseEntity<?> buscarFeedbackPorAluno(@PathVariable Long id) {
        try {
            FeedbackByTeacher feedback = feedbackByTeacherService.buscarFeedbackUnico(id);
            if (feedback == null) {
                return new ResponseEntity<>(Map.of("error", "Feedback não encontrado"), HttpStatus.NOT_FOUND);
            }

            CreatedByDTO teacher = new CreatedByDTO(
                feedback.getCreatedBy().getNomeDocente(),
                feedback.getCreatedBy().getId()
            );
            StudentDTO student = new StudentDTO(
                feedback.getRecipientStudent().getNomeAluno(),
                feedback.getRecipientStudent().getId()
            );
            FeedbackDTO feedbackDTO = new FeedbackDTO(feedback.getConteudo(), teacher, student);

            return new ResponseEntity<>(feedbackDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao buscar feedback: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Busca um feedback direcionado a uma turma pelo ID.
     *
     * @param id ID do feedback a ser buscado.
     * @return ResponseEntity com o feedback no formato DTO ou uma mensagem de erro.
     */
    @GetMapping("/feedbackTeacherClassSt/{id}")
    public ResponseEntity<?> buscarFeedbackPorTurma(@PathVariable Long id) {
        try {
            FeedbackByTeacher feedback = feedbackByTeacherService.buscarFeedbackUnico(id);
            if (feedback == null) {
                return new ResponseEntity<>(Map.of("error", "Feedback não encontrado"), HttpStatus.NOT_FOUND);
            }

            CreatedByDTO teacher = new CreatedByDTO(
                feedback.getCreatedBy().getNomeDocente(),
                feedback.getCreatedBy().getId()
            );
            ClassStDTO classSt = new ClassStDTO(
                feedback.getClassSt().getNomeTurma(),
                feedback.getClassSt().getId()
            );
            Feedback2DTO feedback2DTO = new Feedback2DTO(feedback.getConteudo(), teacher, classSt);

            return new ResponseEntity<>(feedback2DTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao buscar feedback: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
}