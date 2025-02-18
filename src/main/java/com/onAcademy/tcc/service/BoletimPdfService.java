package com.onAcademy.tcc.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class BoletimPdfService {
	
	 private final TemplateEngine templateEngine;

	    public BoletimPdfService(TemplateEngine templateEngine) {
	        this.templateEngine = templateEngine;
	    }

	    public byte[] generatePdf(String templateName, Map<String, Object> data) {
	        String html = templateEngine.process(templateName, new Context(null, data));

	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            PdfRendererBuilder builder = new PdfRendererBuilder();
	            builder.useFastMode();
	            builder.withHtmlContent(html, null);
	            builder.toStream(outputStream);
	            builder.run();
	            return outputStream.toByteArray();

	        } catch (Exception e) {
	            throw new RuntimeException("Erro ao gerar PDF", e);
	        }
	    }
}
