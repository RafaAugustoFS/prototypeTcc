package com.onAcademy.tcc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.onAcademy.tcc.model.Note;
import com.onAcademy.tcc.model.Student;
import com.onAcademy.tcc.service.BoletimPdfService;
import com.onAcademy.tcc.service.BoletimService;

@RestController
@RequestMapping("/api")
public class BoletimPDFController {
    private final BoletimPdfService boletimPdfService;
    private final BoletimService boletimService;
    
    public BoletimPDFController(BoletimPdfService boletimPdfService, BoletimService boletimService) {
        this.boletimPdfService = boletimPdfService;
        this.boletimService = boletimService;
    }
    
    @GetMapping("/boletim/{studentId}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long studentId) {
        Optional<Student> studentOpt = boletimService.getStudentWithGrades(studentId);

        if (studentOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Student student = studentOpt.get();

        // Agrupando notas por disciplina e preenchendo notas ausentes com "-"
        List<Map<String, Object>> disciplinas = student.getNotas().stream()
            .collect(Collectors.groupingBy(n -> n.getDisciplineId().getNomeDisciplina()))
            .entrySet().stream()
            .map(entry -> {
                // Ordenando as notas por bimestre
                List<String> notasOrdenadas = entry.getValue().stream()
                    .sorted(Comparator.comparingInt(Note::getBimestre))
                    .map(n -> n.getNota() != null ? n.getNota().toString() : " - ") // Se a nota for null, substitui por "-"
                    .collect(Collectors.toList());

                // Garantindo que sempre teremos 4 bimestres (preenchendo faltantes com "-")
                while (notasOrdenadas.size() < 4) {
                    notasOrdenadas.add(" - ");
                }

                return Map.of(
                    "nome", entry.getKey(),
                    "notas", notasOrdenadas
                );
            })
            .toList();

        // Construindo os dados para o PDF
        Map<String, Object> data = Map.of(
            "dataAtual", LocalDate.now().toString(),
            "student", Map.of("nomeAluno", student.getNomeAluno()),
            "disciplinas", disciplinas
        );

        try {
            byte[] pdfBytes = boletimPdfService.generatePdf("boletim", data);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=boletim.pdf");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
