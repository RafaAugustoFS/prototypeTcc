package com.onAcademy.tcc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**") // Aplica CORS a todos os endpoints
						.allowedOrigins("http://localhost:3001") // Permite requisições do frontend React/Next.js
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
						.allowedHeaders("*")// Permite qualquer cabeçalho
						// Para busca de imagens também autenticado
						.allowCredentials(true); // Permite envio de cookies e autenticação
			}
		};
	}
}
