package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onAcademy.tcc.model.Reminder;
import com.onAcademy.tcc.repository.ClassStRepo;
import com.onAcademy.tcc.repository.ReminderRepo;

@Service
public class ReminderService {
	@Autowired
	private ReminderRepo reminderRepo;
	
	@Autowired
	private ClassStRepo classStRepo;
	
	public Reminder criarLembrete(Reminder lembrete) {
		Reminder criarLembrete = reminderRepo.save(lembrete);
		return criarLembrete;
	}

	public List<Reminder> buscarTodosLembretes() {
		List<Reminder> buscarLembretes = reminderRepo.findAll();
		return buscarLembretes;
	}

	public List<Reminder> buscarLembretePorClassStId(Long classStId) {
        return reminderRepo.findByClassStId(classStId);
    }
}
