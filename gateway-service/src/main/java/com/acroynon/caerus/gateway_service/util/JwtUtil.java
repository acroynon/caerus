package com.acroynon.caerus.gateway_service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;


@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;
	
	private Algorithm algorithm;
	private JWTVerifier verifier;
	
	public JwtUtil() {
		algorithm = Algorithm.HMAC256("secret".getBytes());
		verifier = JWT.require(algorithm).build();
	}

    public DecodedJWT verifyToken(String token) {
		return verifier.verify(token);
	}
    
    public String[] getRolesFromToken(DecodedJWT decodedJWT){
    	return decodedJWT.getClaim("roles").asArray(String.class);
    }

}
