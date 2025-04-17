package com.onAcademy.tcc.service;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.onAcademy.tcc.config.TokenProvider;
import com.onAcademy.tcc.dto.TeacherDTO;
import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.repository.DisciplineRepo;
import com.onAcademy.tcc.repository.StudentRepo;
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

    public static final String ENROLLMENT_PREFIX = "p";

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private StudentRepo studentRepo;
    
    @Autowired
    private DisciplineRepo disciplineRepo;
    
    @Autowired
    private ImageUploaderService imageUploaderService;

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

        // Adiciona o nome do professor ao final da senha
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
    
    @Transactional
    public Teacher criarTeacher(TeacherDTO teacherDTO) throws MessagingException {
        validarTeacherDTO(teacherDTO);
        
        Teacher teacher = new Teacher();
        List<Discipline> disciplines = disciplineRepo.findAllById(teacherDTO.getDisciplineId());
        if (disciplines.size() != teacherDTO.getDisciplineId().size()) {
            throw new IllegalArgumentException("Algumas disciplinas não foram encontradas");
        }

        // Verifica se há uma imagem em Base64 no DTO
        String imageUrl = null;
        if (teacherDTO.getImageUrl() != null && !teacherDTO.getImageUrl().isEmpty()) {
            imageUrl = imageUploaderService.uploadBase64Image(teacherDTO.getImageUrl());
        }

        teacher.setNomeDocente(teacherDTO.getNomeDocente());
        teacher.setDataNascimentoDocente(teacherDTO.getDataNascimentoDocente());
        teacher.setEmailDocente(teacherDTO.getEmailDocente());
        teacher.setTelefoneDocente(teacherDTO.getTelefoneDocente());
        
        String rawPassword = generateRandomPasswordWithName(6, teacherDTO.getNomeDocente());
        String encoded = passwordEncoder.encode(rawPassword);
        teacher.setPassword(encoded);
        
        if (imageUrl != null) {
            teacher.setImageUrl(imageUrl);
        }

        teacher.setDisciplines(disciplines);
        Teacher savedTeacher = teacherRepo.save(teacher);
        
        String emailSubject = "Bem-vindo ao OnAcademy - Seu cadastro foi realizado com sucesso!";
        String emailText = "<html>" + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                + "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;'>"
                + "<h1 style='color: #007BFF;'>Olá, " + savedTeacher.getNomeDocente() + "!</h1>"
                + "<p style='font-size: 16px;'>Seja muito bem-vindo(a) ao <strong>OnAcademy</strong>! Estamos felizes em tê-lo(a) conosco.</p>"
                + "<p style='font-size: 16px;'>Seu cadastro foi realizado com sucesso. Abaixo estão suas credenciais de acesso:</p>"
                + "<div style='background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0;'>"
                + "<p style='font-size: 14px; margin: 5px 0;'><strong>Código de Matrícula:</strong> "
                + savedTeacher.getIdentifierCode() + "</p>"
                + "<p style='font-size: 14px; margin: 5px 0;'><strong>Senha:</strong> " + rawPassword + "</p>"
                + "</div>"
                + "<p style='font-size: 16px;'>Por favor, mantenha essas informações em local seguro e não as compartilhe com terceiros.</p>"
                + "<p style='font-size: 16px;'>Se precisar de ajuda ou tiver alguma dúvida, entre em contato conosco.</p>"
                + "<p style='font-size: 16px;'>Atenciosamente,<br/><strong>Equipe OnAcademy</strong></p>"
                + "<p style='font-size: 14px; color: #777;'>Este é um e-mail automático, por favor não responda.</p>"
                + "</div>" + "</body>" + "</html>";

        emailService.sendEmail(savedTeacher.getEmailDocente(), emailSubject, emailText);

        return savedTeacher;
    }

    private void validarTeacherDTO(TeacherDTO teacherDTO) {
        if (teacherDTO.getNomeDocente() == null || teacherDTO.getNomeDocente().isEmpty()) {
            throw new IllegalArgumentException("Por favor preencha com um nome.");
        }

        if (!teacherDTO.getNomeDocente().matches("[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ\\s]+")) {
            throw new IllegalArgumentException("O nome deve conter apenas letras.");
        }

        if (teacherDTO.getNomeDocente().length() < 2 || teacherDTO.getNomeDocente().length() > 30) {
            throw new IllegalArgumentException("O nome deve ter entre 2 e 30 caracteres.");
        }

        if (teacherDTO.getDataNascimentoDocente() == null) {
            throw new IllegalArgumentException("Por favor preencha a data de nascimento.");
        }
        
        if (teacherDTO.getEmailDocente() == null || teacherDTO.getEmailDocente().isEmpty()) {
            throw new IllegalArgumentException("Por favor preencha o campo email.");
        }
        
        if (!teacherDTO.getEmailDocente().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("O email fornecido não tem formato válido.");
        }
        
        if (teacherRepo.existsByEmailDocente(teacherDTO.getEmailDocente())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        } else if (studentRepo.existsByEmailAluno(teacherDTO.getEmailDocente())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        if (teacherDTO.getTelefoneDocente() == null || !teacherDTO.getTelefoneDocente().matches("\\d{11}")) {
            throw new IllegalArgumentException("Telefone deve conter exatamente 11 dígitos numéricos.");
        }
        
        if (teacherRepo.existsByTelefoneDocente(teacherDTO.getTelefoneDocente())) {
            throw new IllegalArgumentException("Telefone já cadastrado.");
        }

        if (teacherDTO.getDisciplineId() == null || teacherDTO.getDisciplineId().isEmpty()) {
            throw new IllegalArgumentException("Por favor preencha com no mínimo uma disciplina.");
        }
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
    @Transactional
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
                    + atualizarTeacher.getIdentifierCode() + "</p>"
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
    @Transactional
    public Teacher deletarTeacher(Long id) {
        Optional<Teacher> existingTeacher = teacherRepo.findById(id);
        if (existingTeacher.isPresent()) {
            Teacher teacher = existingTeacher.get();
            teacher.setDisciplines(Collections.emptyList()); // Remover as disciplinas associadas
            teacher.setFeedback(Collections.emptyList()); // Remover os feedbacks recebidos
            teacher.setFeedbackProfessor(Collections.emptyList()); // Remover os feedbacks enviados
            teacher.setFeedbackForm(Collections.emptyList()); // Remover os formulários de feedback

            for (ClassSt turma : teacher.getTeachers()) {
                turma.getClasses().remove(teacher); // Remove o professor da turma
            }
            teacher.setTeachers(Collections.emptyList());
            teacherRepo.delete(teacher);
            return teacher;
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
        return existingTeacher.orElse(null);
    }
}