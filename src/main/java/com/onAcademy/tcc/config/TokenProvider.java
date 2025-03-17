package com.onAcademy.tcc.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class TokenProvider {

	/**
	 * Gera um token JWT para um usuário com suas roles.
	 * 
	 * @param subject O nome do usuário (sujeito do token).
	 * @param roles   A lista de papéis do usuário (roles).
	 * @return O token JWT assinado.
	 */
	public String generate(String subject, List<String> roles) {
		Algorithm algorithm = Algorithm.HMAC256("MySecret");
		return JWT.create().withIssuer("OnAcademy").withClaim("roles", roles).withSubject(subject).sign(algorithm);
	}

	/**
	 * Valida o token JWT.
	 * 
	 * @param token O token JWT a ser validado.
	 * @return O token decodificado se válido, ou null se inválido.
	 */
	public DecodedJWT validateToken(String token) {
		token = token.replace("Bearer ", ""); // Remove o prefixo "Bearer " do token, se presente
		Algorithm algorithm = Algorithm.HMAC256("MySecret"); // Algoritmo de verificação
		try {
			return JWT.require(algorithm).build().verify(token); // Verifica e retorna o token decodificado
		} catch (Exception e) {
			e.printStackTrace(); // Caso ocorra um erro, retorna null
			return null;
		}
	}
}
