package com.onAcademy.tcc.service;

import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageUploaderService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadBase64Image(String base64Image) {
        try {
    
            if (base64Image.contains(",")) {
                base64Image = base64Image.split(",")[1];
            }

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap(
                "resource_type", "image"
            ));
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar imagem para o Cloudinary: " + e.getMessage());
        }
    }
}
