package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.onAcademy.tcc.config.TokenProvider;
import com.onAcademy.tcc.model.Institution;
import com.onAcademy.tcc.repository.InstitutionRepo;

@Service
public class InstitutionService {

	@Autowired
	private InstitutionRepo institutionRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;

	public String loginInstituicao(String identifierCode, String password) {
		Institution institution = institutionRepo.findByidentifierCode(identifierCode)
				.filter(i -> passwordEncoder.matches(password, i.getPassword()))
				.orElseThrow(() -> new RuntimeException("Email ou senha incorretos."));
		return tokenProvider.generate(institution.getId().toString(), List.of("institution"));
	}

	public Institution criarInstituicao(Institution institution) {

		String encoderPassword = passwordEncoder.encode(institution.getPassword());

		institution.setPassword(encoderPassword);

		Institution salvarInstituicao = institutionRepo.save(institution);
		return salvarInstituicao;
	}

	public List<Institution> buscarInstituicao() {
		List<Institution> institution = institutionRepo.findAll();
		return institution;
	}

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
