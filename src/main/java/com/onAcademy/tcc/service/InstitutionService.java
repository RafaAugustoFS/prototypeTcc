package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.onAcademy.tcc.config.TokenProvider;
import com.onAcademy.tcc.model.Institution;
import com.onAcademy.tcc.repository.InstitutionRepo;

/**
 * Serviço responsável pela gestão das instituições na aplicação.
 * 
 * - Este serviço permite realizar operações como login, criação, busca, e
 * atualização de instituições. - Para o login, a classe utiliza o código de
 * identificação (`identifierCode`) e a senha da instituição. - Além disso, a
 * senha é criptografada ao ser salva ou atualizada, utilizando um
 * `PasswordEncoder`.
 * 
 * @see com.onAcademy.tcc.model.Institution
 * @see com.onAcademy.tcc.repository.InstitutionRepo
 * @see org.springframework.security.crypto.password.PasswordEncoder
 */

@Service
public class InstitutionService {

	@Autowired
	private InstitutionRepo institutionRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;

	/**
	 * Realiza o login de uma instituição utilizando o código de identificação e a
	 * senha.
	 * 
	 * - O método verifica se o código de identificação (`identifierCode`) existe no
	 * banco de dados e se a senha informada corresponde à senha armazenada (usando
	 * o `PasswordEncoder`). - Se as credenciais estiverem corretas, é gerado um
	 * token JWT para autenticação e autorização. - Caso as credenciais sejam
	 * inválidas, é lançada uma exceção com a mensagem "Email ou senha incorretos."
	 * 
	 * @param identifierCode O código de identificação da instituição.
	 * @param password       A senha da instituição.
	 * @return Um token JWT gerado para a instituição autenticada.
	 * @throws RuntimeException Se as credenciais estiverem incorretas.
	 */
	public String loginInstituicao(String identifierCode, String password) {
		Institution institution = institutionRepo.findByidentifierCode(identifierCode)
				.filter(i -> passwordEncoder.matches(password, i.getPassword()))
				.orElseThrow(() -> new RuntimeException("Email ou senha incorretos."));
		return tokenProvider.generate(institution.getId().toString(), List.of("institution"));
	}

	/**
	 * Cria uma nova instituição e a salva no banco de dados.
	 * 
	 * - A senha da instituição é criptografada antes de ser salva no banco de
	 * dados, utilizando o `PasswordEncoder`. - Após criptografar a senha, a
	 * instituição é salva no repositório e retornada.
	 * 
	 * @param institution O objeto `Institution` contendo os dados da nova
	 *                    instituição.
	 * @return A instituição recém-criada e salva no banco de dados.
	 */
	public Institution criarInstituicao(Institution institution) {

		String encoderPassword = passwordEncoder.encode(institution.getPassword());

		institution.setPassword(encoderPassword);

		Institution salvarInstituicao = institutionRepo.save(institution);
		return salvarInstituicao;
	}

	/**
	 * Busca todas as instituições registradas no banco de dados.
	 * 
	 * @return Uma lista contendo todas as instituições cadastradas.
	 */
	public List<Institution> buscarInstituicao() {
		List<Institution> institution = institutionRepo.findAll();
		return institution;
	}

	/**
	 * Atualiza os dados de uma instituição existente no banco de dados.
	 * 
	 * - O método encontra a instituição pelo seu ID. Se a instituição existir,
	 * atualiza seus dados (nome, unidade e senha). - A senha é criptografada
	 * novamente antes de ser salva. - Se a instituição não for encontrada, o método
	 * retorna `null`.
	 * 
	 * @param id          O ID da instituição a ser atualizada.
	 * @param institution O objeto `Institution` com os dados atualizados.
	 * @return A instituição atualizada, ou `null` se a instituição não for
	 *         encontrada.
	 */
	public Institution atualizarInstituicao(Long id, Institution institution) {
		Optional<Institution> existInstitution = institutionRepo.findById(id);
		if (existInstitution.isPresent()) {
			Institution atualizarInstituicao = existInstitution.get();
			atualizarInstituicao.setNameInstitution(institution.getNameInstitution());
			atualizarInstituicao.setUnitInstitution(institution.getUnitInstitution());

			String encoderPassword = passwordEncoder.encode(institution.getPassword());
			atualizarInstituicao.setPassword(encoderPassword);

			institutionRepo.save(atualizarInstituicao);
			return atualizarInstituicao;
		}
		return null;
	}
}
