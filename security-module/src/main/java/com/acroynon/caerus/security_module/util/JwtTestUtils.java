package com.acroynon.caerus.security_module.util;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtTestUtils {
    
    protected String secret;
	protected Algorithm algorithm;
	
	public JwtTestUtils(String secret) {
		algorithm = Algorithm.HMAC256(secret.getBytes());
	}

    public String createTestToken(String username, UUID uuid, String... roles) {
		return JWT.create()
			.withSubject(username)
			.withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) 
			.withIssuer("caerus-app")
			.withClaim("roles", Arrays.asList(roles))
			.withClaim("uuid", uuid.toString())
			.sign(algorithm);
	}

}
