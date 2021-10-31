package com.acroynon.caerus.security_module.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret")
	protected String secret;
	
	protected Algorithm algorithm;
	protected JWTVerifier verifier;
	
	public JwtUtil() {
		algorithm = Algorithm.HMAC256("secret".getBytes());
		verifier = JWT.require(algorithm).build();
	}

	public DecodedJWT verifyToken(String token) throws JWTVerificationException{
		return verifier.verify(token);
	}
	
	public UUID getUserId(String token) throws JWTVerificationException {
		DecodedJWT decoded = verifyToken(token);
		return decoded.getClaim("uuid").as(UUID.class);		
	}
	
	public Collection<SimpleGrantedAuthority> authoritiesFromToken(DecodedJWT decodedJWT){
		String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for(String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
	
}
