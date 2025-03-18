package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onAcademy.tcc.model.ClassSt;
import com.onAcademy.tcc.repository.ClassStRepo;

/**
 * Serviço responsável por gerenciar operações relacionadas a turmas (ClassSt).
 */
@Service
public class ClassStService {

    @Autowired
    private ClassStRepo classStRepo;

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
     * @param id       O ID da turma a ser atualizada.
     * @param classSt  Objeto contendo os novos dados da turma.
     * @return A turma atualizada ou null se a turma não existir.
     */
    public ClassSt atualizarClasse(Long id, ClassSt classSt) {
        return classStRepo.findById(id)
                .map(existClass -> {
                    existClass.setNomeTurma(classSt.getNomeTurma());
                    existClass.setAnoLetivoTurma(classSt.getAnoLetivoTurma());
                    existClass.setPeriodoTurma(classSt.getPeriodoTurma());
                    existClass.setCapacidadeMaximaTurma(classSt.getCapacidadeMaximaTurma());
                    existClass.setSalaTurma(classSt.getSalaTurma());
                    return classStRepo.save(existClass);
                })
                .orElse(null);
    }

    /**
     * Remove uma turma pelo seu ID.
     *
     * @param id O ID da turma a ser removida.
     * @return A turma removida ou null se a turma não existir.
     */
    public ClassSt deletarClasse(Long id) {
        return classStRepo.findById(id)
                .map(existClass -> {
                    classStRepo.delete(existClass);
                    return existClass;
                })
                .orElse(null);
    }
}