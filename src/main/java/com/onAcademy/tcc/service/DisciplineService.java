package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.repository.DisciplineRepo;

@Service
public class DisciplineService {

	@Autowired
	private DisciplineRepo disciplineRepo;

	public Discipline criarDiscipline(Discipline discipline) {
		Discipline Salvardiscipline = disciplineRepo.save(discipline);
		return Salvardiscipline;
	}

	public List<Discipline> buscarDisciplines() {
		List<Discipline> buscarDisciplines = disciplineRepo.findAll();
		return buscarDisciplines;
	}

	public Discipline atualizarDiscipline(Long id, Discipline discipline) {
		Optional<Discipline> existingDiscipline = disciplineRepo.findById(id);
		if (existingDiscipline.isPresent()) {
			Discipline atualizarDiscipline = existingDiscipline.get();
			atualizarDiscipline.setNomeDisciplina(discipline.getNomeDisciplina());
			disciplineRepo.save(atualizarDiscipline);
			return atualizarDiscipline;
		}
		return null;
	}

	public Discipline buscarUnicaDisciplina(Long id) {
		Optional<Discipline> existingDiscipline = disciplineRepo.findById(id);
		if (existingDiscipline.isPresent()) {
			Discipline buscarUnicaDisciplina = existingDiscipline.get();
			return buscarUnicaDisciplina;
		}
		return null;
	}

	public Discipline deleteDiscipline(Long id) {
		Optional<Discipline> existingDiscipline = disciplineRepo.findById(id);
		if (existingDiscipline.isPresent()) {
			Discipline deleteDiscipline = existingDiscipline.get();
			disciplineRepo.deleteById(id);
			disciplineRepo.save(deleteDiscipline);
			return deleteDiscipline;
		}
		return null;
	}
}
