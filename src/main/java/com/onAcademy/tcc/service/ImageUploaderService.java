package com.onAcademy.tcc.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageUploaderService {
	@Autowired
	private Cloudinary cloudinary;
	
	public String uploadImage(byte[]imagePath) {
		try {
			Map uploadResult = cloudinary.uploader().upload(imagePath, ObjectUtils.emptyMap());
			return uploadResult.get("url").toString();
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
}
