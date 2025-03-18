package com.onAcademy.tcc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Serviço responsável por enviar e-mails.
 */
@Service
public class EmailService {

    private static final String FROM_EMAIL = "onacademy.tcc@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envia um e-mail para o destinatário especificado.
     *
     * @param to      O endereço de e-mail do destinatário.
     * @param subject O assunto do e-mail.
     * @param text    O conteúdo do e-mail (pode ser HTML).
     * @throws MessagingException Se ocorrer um erro ao enviar o e-mail.
     */
    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(FROM_EMAIL);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        mailSender.send(message);
        System.out.println("E-mail enviado com sucesso para: " + to);
    }
}