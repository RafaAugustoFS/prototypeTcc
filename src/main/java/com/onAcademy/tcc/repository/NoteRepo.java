package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Note;

public interface NoteRepo extends JpaRepository<Note, Long> {

}
