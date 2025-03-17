package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.model.Note;
import com.onAcademy.tcc.model.Student;

public interface NoteRepo extends JpaRepository<Note, Long> {
	 boolean existsByStudentIdAndDisciplineIdAndBimestre(Student studentId, Discipline disciplineId, int bimestre);
	
}
