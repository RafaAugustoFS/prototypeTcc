package com.onAcademy.tcc.repository;

import com.onAcademy.tcc.model.Reminder;
import com.onAcademy.tcc.model.Teacher;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepo extends JpaRepository<Reminder, Long> {
    List<Reminder> findByClassStId(Long classStId);
    List<Reminder> findByCreatedBy(Teacher teacherId);
}
