package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.FeedbackByTeacher;

public interface FeedbackByTeacherRepo extends JpaRepository<FeedbackByTeacher, Long> {

}
