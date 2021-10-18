package com.acroynon.caerus.gateway_service.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {

	@Value("${jwt.secret")
	private String secret;
	
	private Algorithm algorithm;
	private JWTVerifier verifier;
	
	public JwtUtil() {
		algorithm = Algorithm.HMAC256("secret".getBytes());
		verifier = JWT.require(algorithm).build();
	}
	
	public String createToken(String subject, String issuer, List<String> claim, int expirationMins) {
		return JWT.create()
			.withSubject(subject)
			.withExpiresAt(new Date(System.currentTimeMillis() + expirationMins * 60 * 1000)) // 10 mins
			.withIssuer(issuer)
			.withClaim("roles", claim)
			.sign(algorithm);
	}
	
	public DecodedJWT verifyToken(String token) {
		return verifier.verify(token);
	}
	
	public List<String> authoritiesToRoles(Collection<GrantedAuthority> authorities){
		return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
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
