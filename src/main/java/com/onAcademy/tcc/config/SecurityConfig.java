package com.onAcademy.tcc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private SecurityFilter securityFilter;
	
	private static final String [] PERMIT_ALL_LIST = {
			"/swagger-ui/**",
			"/v3/api-docs/**"
	};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuração de segurança HTTP
        http.csrf(csrf -> csrf.disable())  // Desabilita a proteção CSRF (para APIs REST, normalmente é desabilitado)
            .authorizeHttpRequests(authorizedRequests -> {
            	authorizedRequests.requestMatchers("/api/**")  // Permite o acesso sem autenticação para as rotas /api/**
                .permitAll().requestMatchers(PERMIT_ALL_LIST).permitAll()
            	.anyRequest().authenticated();
            }).addFilterBefore(securityFilter, BasicAuthenticationFilter.class);

        return http.build();  // Necessário para configurar o filtro de segurança no Spring Boot 3.x
    }
    
	@Bean
	public PasswordEncoder  passwordEncoder() {
		  return new BCryptPasswordEncoder();
	}
	
	
	
}
