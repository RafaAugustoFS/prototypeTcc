package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.config.TokenProvider;
import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.repository.TeacherRepo;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class TeacherService {

	public static final String ENROLLMENT_PREFIX = "p";

	@Autowired
	private TeacherRepo teacherRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private EmailService emailService;

	

	private String generateRandomPasswordWithName(int length, String nome) {
		String numbers = "0123456789";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			sb.append(numbers.charAt(random.nextInt(numbers.length())));
		}

		// Adiciona o nome do estudante ao final da senha
		String nomeFormatado = nome.replaceAll("\\s+", ""); // Remove espaços em branco do nome
		sb.append(nomeFormatado); // Adiciona o nome formatado à senha

		return sb.toString();
	}

	public String loginTeacher(String identifierCode, String password) {
		Teacher teacher = teacherRepo.findByidentifierCode(identifierCode)
				.filter(i -> passwordEncoder.matches(password, i.getPassword()))
				.orElseThrow(() -> new RuntimeException("Matricula ou senha incorretos"));
		return tokenProvider.generate(teacher.getId().toString(), List.of("teacher"));
	}

	@Transactional
	public Teacher criarTeacher(Teacher teacher) throws MessagingException {
		Teacher teacher1 = new Teacher();
		teacher1.setNomeDocente(teacher.getNomeDocente());
		teacher1.setDataNascimentoDocente(teacher.getDataNascimentoDocente());
		teacher1.setEmailDocente(teacher.getEmailDocente());
		teacher1.setTelefoneDocente(teacher.getTelefoneDocente());

		String year = String.valueOf(teacher.getDataNascimentoDocente().getYear());
		teacher1.setPassword(ENROLLMENT_PREFIX + year + teacher1.getNomeDocente().toLowerCase());

		String rawPassword = Teacher.generateRandomPassword(teacher);
		String encodedPassword = passwordEncoder.encode(rawPassword);
		teacher.setPassword(encodedPassword);

		String encoded = passwordEncoder.encode(teacher1.getPassword());
		teacher1.setPassword(encoded);

		Teacher saveTeacher = teacherRepo.save(teacher1);
		String emailSubject = "Bem-vindo ao OnAcademy - Seu cadastro foi realizado com sucesso!";

		String emailText = "<html>"
		    + "<body style='font-family: Arial, sans-serif; color: #333;'>"
		    + "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;'>"
		    + "<h1 style='color: #007BFF;'>Olá, " + saveTeacher.getNomeDocente() + "!</h1>"
		    + "<p style='font-size: 16px;'>Seja muito bem-vindo(a) ao <strong>OnAcademy</strong>! Estamos felizes em tê-lo(a) conosco.</p>"
		    + "<p style='font-size: 16px;'>Seu cadastro foi realizado com sucesso. Abaixo estão suas credenciais de acesso:</p>"
		    + "<div style='background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0;'>"
		    + "<p style='font-size: 14px; margin: 5px 0;'><strong>Código de Matrícula:</strong> " + saveTeacher.getIdentifierCode() + "</p>"
		    + "<p style='font-size: 14px; margin: 5px 0;'><strong>Senha:</strong> " + rawPassword + "</p>"
		    + "</div>"
		    + "<p style='font-size: 16px;'>Por favor, mantenha essas informações em local seguro e não as compartilhe com terceiros.</p>"
		    + "<p style='font-size: 16px;'>Se precisar de ajuda ou tiver alguma dúvida, entre em contato conosco.</p>"
		    + "<p style='font-size: 16px;'>Atenciosamente,<br/><strong>Equipe OnAcademy</strong></p>"
		    + "<p style='font-size: 14px; color: #777;'>Este é um e-mail automático, por favor não responda.</p>"
		    + "</div>"
		    + "</body>"
		    + "</html>";

		emailService.sendEmail(saveTeacher.getEmailDocente(), emailSubject, emailText);

		return saveTeacher;
	}

	public List<Teacher> buscarTeachers() {
		return teacherRepo.findAll();
	}

	public Teacher atualizarTeacher(Long id, Teacher teacher) {
		Optional<Teacher> existingTeacher = teacherRepo.findById(id);
		if (existingTeacher.isPresent()) {
			Teacher atualizarTeacher = existingTeacher.get();
			atualizarTeacher.setNomeDocente(teacher.getNomeDocente());
			atualizarTeacher.setDataNascimentoDocente(teacher.getDataNascimentoDocente());
			atualizarTeacher.setEmailDocente(teacher.getEmailDocente());
			atualizarTeacher.setTelefoneDocente(teacher.getTelefoneDocente());

			String rawPassword = generateRandomPasswordWithName(6, atualizarTeacher.getNomeDocente());
			String encodedPassword = passwordEncoder.encode(rawPassword);
			atualizarTeacher.setPassword(encodedPassword);
			teacherRepo.save(atualizarTeacher);

			String emailSubject = "Atualização dos seus dados de acesso - Confira as novas informações!";

			String emailText = "<html>" + "<body style='font-family: Arial, sans-serif; color: #333;'>"
					+ "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;'>"
					+ "<h1 style='color: #007BFF;'>Olá, " + atualizarTeacher.getNomeDocente() + "!</h1>"
					+ "<p style='font-size: 16px;'>Seus dados de acesso foram atualizados com sucesso. Abaixo estão suas novas credenciais:</p>"
					+ "<div style='background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0;'>"
					+ "<p style='font-size: 14px; margin: 5px 0;'><strong>Código de Matrícula:</strong> "
					+"</p>"
					+ "<p style='font-size: 14px; margin: 5px 0;'><strong>Nova Senha:</strong> " + rawPassword + "</p>"
					+ "</div>"
					+ "<p style='font-size: 16px;'>Por favor, mantenha essas informações em local seguro e não as compartilhe com terceiros.</p>"
					+ "<p style='font-size: 16px;'>Se você não solicitou essa alteração ou tem alguma dúvida, entre em contato conosco imediatamente.</p>"
					+ "<p style='font-size: 16px;'>Atenciosamente,<br/><strong>Equipe OnAcademy</strong></p>"
					+ "<p style='font-size: 14px; color: #777;'>Este é um e-mail automático, por favor não responda.</p>"
					+ "</div>" + "</body>" + "</html>";
			try {
				emailService.sendEmail(atualizarTeacher.getEmailDocente(), emailSubject, emailText);
			} catch (MessagingException e) {
				throw new RuntimeException("Erro ao enviar email com os novos dados de acesso.", e);
			}

			return atualizarTeacher;
		}
		return null;
	}

	public Teacher deletarTeacher(Long id) {
		Optional<Teacher> existingTeacher = teacherRepo.findById(id);
		if (existingTeacher.isPresent()) {
			Teacher deletarTeacher = existingTeacher.get();
			teacherRepo.delete(deletarTeacher);
			return deletarTeacher;
		}
		return null;
	}

	public Teacher buscarUnicoTeacher(Long id) {
		Optional<Teacher> existingTeacher = teacherRepo.findById(id);
		if (existingTeacher.isPresent()) {
			return existingTeacher.get();
		}
		return null;
	}

}
