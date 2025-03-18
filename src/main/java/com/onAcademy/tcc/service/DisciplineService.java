package com.onAcademy.tcc.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.repository.DisciplineRepo;

/**
 * Serviço responsável por operações relacionadas a disciplinas.
 */
@Service
public class DisciplineService {

    @Autowired
    private DisciplineRepo disciplineRepo;

    /**
     * Cria uma nova disciplina.
     *
     * @param discipline A disciplina a ser criada.
     * @return A disciplina salva.
     */
    public Discipline criarDiscipline(Discipline discipline) {
        return disciplineRepo.save(discipline);
    }

    /**
     * Retorna uma lista de todas as disciplinas cadastradas.
     *
     * @return Lista de disciplinas.
     */
    public List<Discipline> buscarDisciplines() {
        return disciplineRepo.findAll();
    }

    /**
     * Atualiza os dados de uma disciplina existente.
     *
     * @param id         O ID da disciplina a ser atualizada.
     * @param discipline Objeto contendo os novos dados da disciplina.
     * @return A disciplina atualizada ou null se a disciplina não existir.
     */
    public Discipline atualizarDiscipline(Long id, Discipline discipline) {
        return disciplineRepo.findById(id)
                .map(existingDiscipline -> {
                    existingDiscipline.setNomeDisciplina(discipline.getNomeDisciplina());
                    return disciplineRepo.save(existingDiscipline);
                })
                .orElse(null);
    }

    /**
     * Busca uma disciplina pelo seu ID.
     *
     * @param id O ID da disciplina a ser buscada.
     * @return A disciplina encontrada ou null se não existir.
     */
    public Discipline buscarUnicaDisciplina(Long id) {
        return disciplineRepo.findById(id).orElse(null);
    }

    /**
     * Remove uma disciplina pelo seu ID.
     *
     * @param id O ID da disciplina a ser removida.
     * @return A disciplina removida ou null se a disciplina não existir.
     */
    public Discipline deleteDiscipline(Long id) {
        return disciplineRepo.findById(id)
                .map(existingDiscipline -> {
                    disciplineRepo.deleteById(id);
                    return existingDiscipline;
                })
                .orElse(null);
    }
}