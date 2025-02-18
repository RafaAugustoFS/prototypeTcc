package com.onAcademy.tcc.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class TokenProvider {
	public String generate(String subject, List<String> roles) {
		Algorithm algorithm = Algorithm.HMAC256("MySecret");
		return JWT.create().withIssuer("OnAcademy").withClaim("roles", roles).withSubject(subject).sign(algorithm);
	}

	public DecodedJWT validateToken(String token) {
		token = token.replace("Bearer ", "");
		Algorithm algorithm = Algorithm.HMAC256("MySecret");
		try {
			return JWT.require(algorithm).build().verify(token);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
