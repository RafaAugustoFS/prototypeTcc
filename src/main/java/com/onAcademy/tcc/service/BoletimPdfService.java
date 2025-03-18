package com.onAcademy.tcc.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Serviço responsável por gerar PDFs a partir de templates HTML.
 */
@Service
public class BoletimPdfService {

    private final TemplateEngine templateEngine;

    /**
     * Construtor para injetar a dependência do TemplateEngine.
     *
     * @param templateEngine O motor de template Thymeleaf.
     */
    public BoletimPdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Gera um PDF a partir de um template HTML e dados fornecidos.
     *
     * @param templateName O nome do template HTML a ser processado.
     * @param data         Os dados a serem inseridos no template.
     * @return Um array de bytes representando o PDF gerado.
     * @throws RuntimeException Se ocorrer um erro durante a geração do PDF.
     */
    public byte[] generatePdf(String templateName, Map<String, Object> data) {
        // Processa o template HTML com os dados fornecidos
        String htmlContent = templateEngine.process(templateName, new Context(null, data));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder pdfBuilder = new PdfRendererBuilder();
            pdfBuilder.useFastMode();
            pdfBuilder.withHtmlContent(htmlContent, null);
            pdfBuilder.toStream(outputStream);
            pdfBuilder.run();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}