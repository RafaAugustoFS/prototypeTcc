package com.onAcademy.tcc.controller;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.onAcademy.tcc.model.Teacher;
import com.onAcademy.tcc.model.Discipline;
import com.onAcademy.tcc.service.TeacherService;
import com.onAcademy.tcc.repository.DisciplineRepo;
import com.onAcademy.tcc.repository.TeacherRepo;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Teacher", description = "EndPoint de professor")
@RestController
@RequestMapping("/api")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/teacher")
    @PreAuthorize("hasRole('INSTITUTION')")
    public ResponseEntity<?> criarTeacher(@RequestBody Teacher teacher) {
        try {
            teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
            Teacher savedTeacher = teacherRepo.save(teacher);
            return new ResponseEntity<>(savedTeacher, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao criar professor: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/teacher")
    public ResponseEntity<?> buscarTeachers() {
        try {
            List<Teacher> teachers = teacherService.buscarTeachers();
            return new ResponseEntity<>(teachers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao buscar professores: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<?> buscarTeacherUnico(@PathVariable Long id) {
        try {
            Teacher teacher = teacherService.buscarUnicoTeacher(id);
            if (teacher == null) {
                return new ResponseEntity<>(Map.of("error", "Professor não encontrado"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(teacher, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao buscar professor: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/teacher/{id}")
    public ResponseEntity<?> editarTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        try {
            Teacher atualizarTeacher = teacherService.atualizarTeacher(id, teacher);
            if (atualizarTeacher == null) {
                return new ResponseEntity<>(Map.of("error", "Professor não encontrado"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(atualizarTeacher, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao atualizar professor: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/teacher/{id}")
    public ResponseEntity<?> deletarTeacher(@PathVariable Long id) {
        try {
            Teacher deletarTeacher = teacherService.deletarTeacher(id);
            if (deletarTeacher == null) {
                return new ResponseEntity<>(Map.of("error", "Professor não encontrado"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(deletarTeacher, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Erro ao deletar professor: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
