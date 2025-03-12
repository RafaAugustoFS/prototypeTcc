package com.onAcademy.tcc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.FeedbackForm;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.model.Teacher;

public interface FeedbackFormRepo extends JpaRepository<FeedbackForm, Long> {
    boolean existsByCreatedByAndRecipientStudentAndBimestre(Teacher createdBy, Student recipientStudent, int bimestre);
    List<FeedbackForm> findByRecipientStudentId(Long id);
}