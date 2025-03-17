package com.onAcademy.tcc.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// A anotação @Component torna a classe um bean gerenciado pelo Spring.
@Component
public class SecurityFilter extends OncePerRequestFilter {

	// A classe TokenProvider é injetada no filtro.
	@Autowired
	private TokenProvider tokenProvider;

	/**
	 * Filtro de segurança para validar tokens JWT e autenticar usuários.
	 * 
	 * - Verifica o cabeçalho "Authorization" em cada requisição. - Se o token for
	 * válido, autentica o usuário e atribui suas permissões. - Se o token for
	 * inválido, retorna o status HTTP 401 (Unauthorized).
	 * 
	 * Esse filtro garante que apenas usuários autenticados possam acessar os
	 * recursos protegidos da aplicação.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (header != null) {
			var token = tokenProvider.validateToken(header);
			if (token == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			request.setAttribute("resourceId", token);
			var roles = token.getClaim("roles").asList(String.class);
			var grants = roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())).toList();
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					token.getSubject(), null, grants);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		filterChain.doFilter(request, response);
	}

}
