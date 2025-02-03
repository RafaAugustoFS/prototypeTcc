package com.onAcademy.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onAcademy.tcc.model.Event;

public interface EventRepo extends JpaRepository<Event, Long> {

}
