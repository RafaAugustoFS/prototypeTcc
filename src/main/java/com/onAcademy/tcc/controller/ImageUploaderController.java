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

    @PostMapping("/image")
    public ResponseEntity<Object> uploadBase64(@RequestBody Map<String, String> request) {
        try {
            String base64Image = request.get("image");
            String imageUrl = imageUploaderService.uploadBase64Image(base64Image);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
