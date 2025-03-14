package com.onAcademy.tcc.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

	/**
	 * Configura e fornece um bean do Cloudinary com as credenciais necessárias. As
	 * credenciais são carregadas a partir das propriedades configuradas no
	 * application.properties.
	 */

	@Value("${cloudinary.cloud_name}")
	private String cloudName;
	@Value("${cloudinary.api_key}")
	private String apiKey;
	@Value("${cloudinary.api_secret}")
	private String apiSecret;

	/**
	 * @ Bean = Método que cria um objeto que ele vai gerenciar. Esse objeto pode
	 * ser usado em outras partes do projeto.
	 */

	@Bean
	public Cloudinary cloudinary() {
		Map<String, String> config = new HashMap();
		config.put("cloud_name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		return new Cloudinary(config);
	}
}
