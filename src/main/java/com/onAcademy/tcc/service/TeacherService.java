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

/**
 * Serviço responsável pela gestão dos professores no sistema.
 * 
 * - Este serviço permite realizar operações como login, criação, atualização,
 * exclusão e busca de professores. - Também oferece funcionalidade para gerar
 * senhas aleatórias para os professores e enviar e-mails com as credenciais de
 * acesso.
 * 
 * @see com.onAcademy.tcc.model.Teacher
 * @see com.onAcademy.tcc.repository.TeacherRepo
 */

@Service
public class TeacherService {

	// Prefixo para matrícula do professor
	public static final String ENROLLMENT_PREFIX = "p";

	@Autowired
	private TeacherRepo teacherRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private EmailService emailService;

	/**
	 * Gera uma senha aleatória para o professor, utilizando o nome e um número
	 * aleatório.
	 * 
	 * @param length O comprimento da parte numérica da senha.
	 * @param nome   O nome do professor, que será incluído na senha.
	 * @return A senha gerada com números aleatórios e o nome do professor.
	 */
	private String generateRandomPasswordWithName(int length, String nome) {
		String numbers = "0123456789";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			sb.append(numbers.charAt(random.nextInt(numbers.length())));
		}

		// Adiciona o nome do teacher ao final da senha
		String nomeFormatado = nome.replaceAll("\\s+", ""); // Remove espaços em branco do nome
		sb.append(nomeFormatado); // Adiciona o nome formatado à senha

		return sb.toString();
	}

	/**
	 * Realiza o login do professor no sistema.
	 * 
	 * @param identifierCode O código de matrícula do professor.
	 * @param password       A senha fornecida pelo professor.
	 * @return Um token JWT gerado para o professor logado.
	 * @throws RuntimeException Se a matrícula ou a senha estiverem incorretas.
	 */
	public String loginTeacher(String identifierCode, String password) {
		Teacher teacher = teacherRepo.findByIdentifierCode(identifierCode)
				.filter(i -> passwordEncoder.matches(password, i.getPassword()))
				.orElseThrow(() -> new RuntimeException("Matricula ou senha incorretos"));
		return tokenProvider.generate(teacher.getId().toString(), List.of("teacher"));
	}

	/**
	 * Cria um novo professor no sistema.
	 * 
	 * - Valida os dados fornecidos. - Gera uma senha aleatória, codifica e salva o
	 * professor. - Envia um e-mail com as credenciais de acesso.
	 * 
	 * @param teacher O objeto `Teacher` contendo os dados do professor.
	 * @return O professor criado e salvo no banco de dados.
	 * @throws MessagingException Se ocorrer um erro ao enviar o e-mail.
	 */
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

		String emailText = "<html>" + "<body style='font-family: Arial, sans-serif; color: #333;'>"
				+ "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;'>"
				+ "<h1 style='color: #007BFF;'>Olá, " + saveTeacher.getNomeDocente() + "!</h1>"
				+ "<p style='font-size: 16px;'>Seja muito bem-vindo(a) ao <strong>OnAcademy</strong>! Estamos felizes em tê-lo(a) conosco.</p>"
				+ "<p style='font-size: 16px;'>Seu cadastro foi realizado com sucesso. Abaixo estão suas credenciais de acesso:</p>"
				+ "<div style='background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0;'>"
				+ "<p style='font-size: 14px; margin: 5px 0;'><strong>Código de Matrícula:</strong> "
				+ saveTeacher.getIdentifierCode() + "</p>"
				+ "<p style='font-size: 14px; margin: 5px 0;'><strong>Senha:</strong> " + rawPassword + "</p>"
				+ "</div>"
				+ "<p style='font-size: 16px;'>Por favor, mantenha essas informações em local seguro e não as compartilhe com terceiros.</p>"
				+ "<p style='font-size: 16px;'>Se precisar de ajuda ou tiver alguma dúvida, entre em contato conosco.</p>"
				+ "<p style='font-size: 16px;'>Atenciosamente,<br/><strong>Equipe OnAcademy</strong></p>"
				+ "<p style='font-size: 14px; color: #777;'>Este é um e-mail automático, por favor não responda.</p>"
				+ "</div>" + "</body>" + "</html>";

		emailService.sendEmail(saveTeacher.getEmailDocente(), emailSubject, emailText);

		return saveTeacher;
	}

	/**
	 * Busca todos os professores cadastrados no sistema.
	 * 
	 * @return Uma lista de todos os professores.
	 */
	public List<Teacher> buscarTeachers() {
		return teacherRepo.findAll();
	}

	/**
	 * Atualiza os dados de um professor existente no sistema.
	 * 
	 * - Gera uma nova senha, codifica e envia um e-mail notificando o professor
	 * sobre a atualização.
	 * 
	 * @param id      O ID do professor a ser atualizado.
	 * @param teacher O objeto `Teacher` contendo os novos dados do professor.
	 * @return O professor atualizado e salvo no banco de dados.
	 */
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
					+ "<p style='font-size: 14px; margin: 5px 0;'><strong>Código de Matrícula:</strong> " + "</p>"
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

	/**
	 * Exclui um professor do sistema.
	 * 
	 * @param id O ID do professor a ser excluído.
	 * @return O professor excluído, ou `null` caso não exista.
	 */
	public Teacher deletarTeacher(Long id) {
		Optional<Teacher> existingTeacher = teacherRepo.findById(id);
		if (existingTeacher.isPresent()) {
			Teacher deletarTeacher = existingTeacher.get();
			teacherRepo.delete(deletarTeacher);
			return deletarTeacher;
		}
		return null;
	}

	/**
	 * Busca um professor específico pelo ID.
	 * 
	 * @param id O ID do professor.
	 * @return O professor encontrado, ou `null` caso não exista.
	 */
	public Teacher buscarUnicoTeacher(Long id) {
		Optional<Teacher> existingTeacher = teacherRepo.findById(id);
		if (existingTeacher.isPresent()) {
			return existingTeacher.get();
		}
		return null;
	}

}
