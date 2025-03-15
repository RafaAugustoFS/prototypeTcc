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

	/**
	 * Construtor para injeção de dependências.
	 *
	 * @param boletimPdfService Serviço para geração de PDFs.
	 * @param boletimService    Serviço para obtenção de dados do boletim.
	 */
	public BoletimPDFController(BoletimPdfService boletimPdfService, BoletimService boletimService) {
		this.boletimPdfService = boletimPdfService;
		this.boletimService = boletimService;
	}

	/**
	 * Endpoint para download do boletim do aluno em formato PDF.
	 *
	 * @param studentId ID do aluno.
	 * @return ResponseEntity contendo o arquivo PDF ou um status de erro.
	 */
	@GetMapping("/boletim/{studentId}")
	public ResponseEntity<byte[]> downloadPdf(@PathVariable Long studentId) {
		Optional<Student> studentOpt = boletimService.getStudentWithGrades(studentId);

		if (studentOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Student student = studentOpt.get();
		List<Map<String, Object>> disciplinas = processarNotasPorDisciplina(student);

		Map<String, Object> pdfData = criarDadosParaPdf(student, disciplinas);

		try {
			byte[] pdfBytes = boletimPdfService.generatePdf("boletim", pdfData);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=boletim.pdf");
			return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Processa as notas do aluno, agrupando-as por disciplina e garantindo 4
	 * bimestres.
	 *
	 * @param student Aluno com as notas a serem processadas.
	 * @return Lista de disciplinas com as notas organizadas.
	 */
	private List<Map<String, Object>> processarNotasPorDisciplina(Student student) {
		return student.getNotas().stream().collect(Collectors.groupingBy(n -> n.getDisciplineId().getNomeDisciplina()))
				.entrySet().stream().map(entry -> {
					List<String> notasOrdenadas = entry.getValue().stream()
							.sorted(Comparator.comparingInt(Note::getBimestre))
							.map(n -> n.getNota() != null ? n.getNota().toString() : " - ")
							.collect(Collectors.toList());

					while (notasOrdenadas.size() < 4) {
						notasOrdenadas.add(" - ");
					}

					return Map.of("nome", entry.getKey(), "notas", notasOrdenadas);
				}).toList();
	}

	/**
	 * Cria os dados necessários para a geração do PDF.
	 *
	 * @param student     Aluno com os dados a serem incluídos no PDF.
	 * @param disciplinas Lista de disciplinas com as notas processadas.
	 * @return Mapa contendo os dados formatados para o PDF.
	 */
	private Map<String, Object> criarDadosParaPdf(Student student, List<Map<String, Object>> disciplinas) {
		return Map.of("dataAtual", LocalDate.now().toString(), "student", Map.of("nomeAluno", student.getNomeAluno()),
				"disciplinas", disciplinas);
	}
}