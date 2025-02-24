package com.onAcademy.tcc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.FeedBackByStudent;

public interface FeedbackByStudentRepo extends JpaRepository<FeedBackByStudent, Long> {
	List<FeedBackByStudent> findByRecipientTeacher_Id(Long recipientTeacherId);
}
