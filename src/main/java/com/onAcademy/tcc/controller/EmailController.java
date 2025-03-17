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

	/**
	 * Envia um e-mail com os dados fornecidos na requisição.
	 * 
	 * - Recebe os dados do e-mail (destinatário, assunto, corpo) via
	 * `EmailRequest`. - Utiliza o serviço `EmailService` para enviar o e-mail. - Se
	 * o envio for bem-sucedido, retorna a resposta HTTP 200 com uma mensagem de
	 * sucesso. - Se ocorrer um erro durante o envio, retorna a resposta HTTP 500
	 * com uma mensagem de erro.
	 * 
	 * @param request Objeto contendo as informações do e-mail (destinatário,
	 *                assunto, corpo).
	 * @return Resposta com status HTTP e mensagem sobre o envio do e-mail.
	 */
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
