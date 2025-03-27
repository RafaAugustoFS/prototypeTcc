package com.onAcademy.tcc.model;
 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
 
/**
* Representa um formulário de feedback preenchido por um professor para um estudante.
* Esta entidade armazena as respostas do formulário, o bimestre ao qual o feedback se refere,
* e os relacionamentos com o professor que criou o feedback e o estudante que o recebeu.
*
* @param id               Identificador único do formulário de feedback.
* @param resposta1        Resposta da pergunta 1 do formulário.
* @param resposta2        Resposta da pergunta 2 do form	ulário.
* @param resposta3        Resposta da pergunta 3 do formulário.
* @param resposta4        Resposta da pergunta 4 do formulário.
* @param resposta5        Resposta da pergunta 5 do formulário.
* @param bimestre         Bimestre ao qual o feedback se refere.
* @param createdBy        Professor que criou o formulário de feedback.
* @param recipientStudent Estudante que recebeu o feedback.
*/
@Entity
@Data
public class FeedbackForm {
    @Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private int resposta1;
    private int resposta2;
    private int resposta3;
    private int resposta4;
    private int resposta5;
    private int bimestre;
 
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher createdBy;
    
    @ManyToOne
    @JoinColumn(name = "recipient_student_id")
    private Student recipientStudent;
    
    public Long getIdTurma() {
        if (this.recipientStudent != null && this.recipientStudent.getClassSt() != null) {
            return this.recipientStudent.getClassSt().getId();
        }
        return null;
    }
    
 
}