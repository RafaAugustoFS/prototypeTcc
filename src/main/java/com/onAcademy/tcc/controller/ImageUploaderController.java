package com.onAcademy.tcc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onAcademy.tcc.service.ImageUploaderService;

@RestController
@RequestMapping("/api")
public class ImageUploaderController {
	@Autowired
	private ImageUploaderService imageUploaderService;
	
	@PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> imageUploader(@RequestPart("file") MultipartFile file) {
		try {
			String imageUrl = imageUploaderService.uploadImage(file.getBytes());
			return ResponseEntity.ok(imageUrl);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e);
		}
	}
	
}
