package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.repository.ClassStRepo;

import jakarta.transaction.Transactional;

@Service
public class ClassStService {
	@Autowired
	private ClassStRepo classStRepo;

	@Transactional
	public List<ClassSt> buscarTodasClasses() {
		List<ClassSt> buscarClasses = classStRepo.findAll();
		return buscarClasses;
	}

	public ClassSt buscarClasseUnica(Long id) {
		Optional<ClassSt> existClass = classStRepo.findById(id);
		if (existClass.isPresent()) {
			return existClass.get();
		}
		return null;
	}

	public ClassSt atualizarClasse(Long id, ClassSt classSt) {
		Optional<ClassSt> existClass = classStRepo.findById(id);
		if (existClass.isPresent()) {
			ClassSt atualizarClassSt = existClass.get();
			atualizarClassSt.setNomeTurma(classSt.getNomeTurma());
			atualizarClassSt.setAnoLetivoTurma(classSt.getAnoLetivoTurma());
			atualizarClassSt.setPeriodoTurma(classSt.getPeriodoTurma());
			atualizarClassSt.setCapacidadeMaximaTurma(classSt.getCapacidadeMaximaTurma());
			atualizarClassSt.setSalaTurma(classSt.getSalaTurma());
			classStRepo.save(atualizarClassSt);
			return atualizarClassSt;
		}
		return null;
	}

	public ClassSt deletarClasse(Long id) {
		Optional<ClassSt> existClass = classStRepo.findById(id);
		if (existClass.isPresent()) {
			ClassSt deletarClass = existClass.get();
			classStRepo.delete(deletarClass);
			return deletarClass;
		}
		return null;
	}
}
