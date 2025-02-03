package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.Institution;
import com.onAcademy.tcc.repository.InstitutionRepo;

@Service
public class InstitutionService {
	
	@Autowired
	private InstitutionRepo institutionRepo;
	
	public Institution criarInstituicao(Institution institution) {
		Institution salvarInstituicao = institutionRepo.save(institution);
		return salvarInstituicao;
	}
	public List<Institution> buscarInstituicao() {
		List<Institution> institution = institutionRepo.findAll();
		return institution;
	}
	public Institution atualizarInstituicao(Long id, Institution institution) {
		Optional<Institution> existInstitution = institutionRepo.findById(id);
		if(existInstitution.isPresent()) {
			Institution atualizarInstituicao = existInstitution.get();
			atualizarInstituicao.setNameInstitution(institution.getNameInstitution());
			atualizarInstituicao.setUnitInstitution(institution.getUnitInstitution());
			institutionRepo.save(atualizarInstituicao);
			return atualizarInstituicao;
		}
		return null;
	}
}
