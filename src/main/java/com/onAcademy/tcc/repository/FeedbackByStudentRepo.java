package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.FeedBackByStudent;

public interface FeedbackByStudentRepo extends JpaRepository<FeedBackByStudent, Long> {

}
