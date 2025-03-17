package com.onAcademy.tcc.model;

/**
 * Representa uma solicitação de envio de e-mail.
 * Esta classe é usada para encapsular os dados necessários para enviar um e-mail,
 * como o destinatário, o assunto e o corpo da mensagem.
 *
 * @param to      Endereço de e-mail do destinatário.
 * @param subject Assunto do e-mail.
 * @param body    Corpo da mensagem do e-mail.
 */
public class EmailRequest {
    private String to;
    private String subject;
    private String body;

    // Getters e Setters
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}