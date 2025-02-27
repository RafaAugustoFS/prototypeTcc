package com.onAcademy.tcc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onAcademy.tcc.model.EmailRequest;
import com.onAcademy.tcc.service.EmailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        try {
            emailService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
            return ResponseEntity.ok("E-mail enviado com sucesso para " + request.getTo());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}
