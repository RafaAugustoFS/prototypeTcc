package com.onAcademy.tcc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onAcademy.tcc.service.ImageUploaderService;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImageUploaderController {

    @Autowired
    private ImageUploaderService imageUploaderService;

    /**
     * Faz o upload de uma imagem em formato base64.
     *
     * @param request Mapa contendo a imagem em formato base64 na chave "image".
     * @return ResponseEntity com a URL da imagem enviada ou uma mensagem de erro.
     */
    @PostMapping("/image")
    public ResponseEntity<Object> uploadBase64(@RequestBody Map<String, String> request) {
        try {
            String base64Image = request.get("image");
            if (base64Image == null || base64Image.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("A imagem em base64 é obrigatória.");
            }

            String imageUrl = imageUploaderService.uploadBase64Image(base64Image);
            return ResponseEntity.ok(imageUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao processar o upload da imagem: " + e.getMessage());
        }
    }
}