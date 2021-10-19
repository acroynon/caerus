package com.acroynon.caerus.gateway_service.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
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
	private String secret;
	
	private Algorithm algorithm;
	private JWTVerifier verifier;
	
	public JwtUtil() {
		algorithm = Algorithm.HMAC256("secret".getBytes());
		verifier = JWT.require(algorithm).build();
	}
	
	public Map<String, String> generateTokenPair(String username, UUID uuid, List<String> roles){
		String accessToken = createToken(username, uuid, roles, 10);
		String refreshToken = createToken(username, uuid, roles, 30);
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", accessToken);
		tokens.put("refresh_token", refreshToken);
		return tokens;
	}
	
	public DecodedJWT verifyToken(String token) throws JWTVerificationException{
		return verifier.verify(token);
	}
	
	public boolean isTokenExpired(DecodedJWT decoded) {
		return decoded.getExpiresAt().before(new Date());
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
	
	private String createToken(String username, UUID uuid, List<String> roles, int expirationMins) {
		return JWT.create()
			.withSubject(username)
			.withExpiresAt(new Date(System.currentTimeMillis() + expirationMins * 60 * 1000)) 
			.withIssuer("caerus-app")
			.withClaim("roles", roles)
			.withClaim("uuid", uuid.toString())
			.sign(algorithm);
	}
	
}
