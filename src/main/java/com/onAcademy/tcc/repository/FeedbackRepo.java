package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Feedback;

public interface FeedbackRepo extends JpaRepository<Feedback, Long> {

}
