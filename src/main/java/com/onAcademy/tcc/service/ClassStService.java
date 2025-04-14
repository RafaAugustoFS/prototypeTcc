package com.onAcademy.tcc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.repository.ClassStRepo;
import com.onAcademy.tcc.repository.StudentRepo;

/**
 * Serviço responsável por gerenciar operações relacionadas a turmas (ClassSt).
 */
@Service
public class ClassStService {

	@Autowired
	private ClassStRepo classStRepo;

	@Autowired
	private StudentRepo studentRepo;

	/**
	 * Retorna uma lista de todas as turmas cadastradas.
	 *
	 * @return Lista de turmas.
	 */
	@Transactional
	public List<ClassSt> buscarTodasClasses() {
		return classStRepo.findAll();
	}

	/**
	 * Busca uma turma pelo seu ID.
	 *
	 * @param id O ID da turma a ser buscada.
	 * @return A turma encontrada ou null se não existir.
	 */
	public ClassSt buscarClasseUnica(Long id) {
		return classStRepo.findById(id).orElse(null);
	}

	/**
	 * Atualiza os dados de uma turma existente.
	 *
	 * @param id      O ID da turma a ser atualizada.
	 * @param classSt Objeto contendo os novos dados da turma.
	 * @return A turma atualizada ou null se a turma não existir.
	 */
	public ClassSt atualizarClasse(Long id, ClassSt classSt) {
		return classStRepo.findById(id).map(existClass -> {
			existClass.setNomeTurma(classSt.getNomeTurma());
			existClass.setAnoLetivoTurma(classSt.getAnoLetivoTurma());
			existClass.setPeriodoTurma(classSt.getPeriodoTurma());
			existClass.setCapacidadeMaximaTurma(classSt.getCapacidadeMaximaTurma());
			existClass.setSalaTurma(classSt.getSalaTurma());

			// Atualiza professores (se fornecido)
			if (classSt.getClasses() != null) {
				existClass.getClasses().clear();
				existClass.getClasses().addAll(classSt.getClasses());
			}

			// Atualiza disciplinas (se fornecido)
			if (classSt.getDisciplinaTurmas() != null) {
				existClass.getDisciplinaTurmas().clear();
				existClass.getDisciplinaTurmas().addAll(classSt.getDisciplinaTurmas());
			}

			return classStRepo.save(existClass);
		}).orElse(null);
	}

	/**
	 * Remove uma turma pelo seu ID.
	 *
	 * @param id O ID da turma a ser removida.
	 * @return A turma removida ou null se a turma não existir.
	 */
	public ClassSt deletarClasse(Long id) {
		return classStRepo.findById(id).map(existClass -> {
			List<Student> students = studentRepo.findByTurmaId(id);
			for (Student student : students) {
				student.setTurmaId(null); // Desassocia a turma dos alunos
				student.setClassSt(null); // Se estiver utilizando relacionamento bidirecional
				studentRepo.save(student); // Salva a atualização
			}
			classStRepo.delete(existClass);
			return existClass;
		}).orElse(null);
	}
}