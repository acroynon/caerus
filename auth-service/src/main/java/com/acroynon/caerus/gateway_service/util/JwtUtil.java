package com.acroynon.caerus.gateway_service.util;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;

@Component
public class JwtUtil extends com.acroynon.caerus.security_module.util.JwtUtil {
	
	public Map<String, String> generateTokenPair(String username, UUID uuid, List<String> roles){
		String accessToken = createToken(username, uuid, roles, 10);
		String refreshToken = createToken(username, uuid, roles, 30);
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", accessToken);
		tokens.put("refresh_token", refreshToken);
		return tokens;
	}
	
	public List<String> authoritiesToRoles(Collection<GrantedAuthority> authorities){
		return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
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
