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

    private static final String[] PUBLIC_ENDPOINTS = {
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/api/**",
        "/student/upload-image/{id}**",
        "/student/image/{id}**"
    };

    /**
     * Configura a cadeia de filtros de segurança para a aplicação.
     * Desabilita a proteção CSRF para APIs REST e define quais endpoints são públicos.
     * Adiciona um filtro personalizado antes do filtro de autenticação básica.
     *
     * @param http Configuração de segurança HTTP.
     * @return SecurityFilterChain configurado.
     * @throws Exception Em caso de erro na configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs REST
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll() // Permite acesso público aos endpoints listados
                .anyRequest().authenticated() // Exige autenticação para todos os outros endpoints
            )
            .addFilterBefore(securityFilter, BasicAuthenticationFilter.class); // Adiciona filtro personalizado

        return http.build();
    }

    /**
     * Configura o encoder de senhas usando BCrypt.
     *
     * @return Uma instância de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Configura o encoder de senhas
    }
}