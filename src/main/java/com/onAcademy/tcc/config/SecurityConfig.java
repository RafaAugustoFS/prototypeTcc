package com.onAcademy.tcc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuração de segurança HTTP
        http.csrf().disable()  // Desabilita a proteção CSRF (para APIs REST, normalmente é desabilitado)
            .authorizeRequests()
            .requestMatchers("/api/**").permitAll()  // Permite o acesso sem autenticação para as rotas /api/**
            .anyRequest().authenticated()      // Exige autenticação para outras rotas
            .and()
            .formLogin().disable();  // Desabilita o login padrão, se não for necessário

        return http.build();  // Necessário para configurar o filtro de segurança no Spring Boot 3.x
    }
    
	@Bean
	public PasswordEncoder  passWordEncoder() {
		  return new BCryptPasswordEncoder();
	}
	
}
