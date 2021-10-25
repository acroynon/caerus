package com.acroynon.caerus.gateway_service.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acroynon.caerus.gateway_service.dto.AuthenticateDTO;
import com.acroynon.caerus.gateway_service.dto.RegisterDTO;
import com.acroynon.caerus.gateway_service.model.User;
import com.acroynon.caerus.gateway_service.service.UserService;
import com.acroynon.caerus.gateway_service.util.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController @RequiredArgsConstructor @Slf4j
public class AuthController {
	
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> register(@RequestBody RegisterDTO dto) {
		String username = dto.getUsername();
		String password = dto.getPassword();
		String confirmPassword = dto.getConfirmPassword();
		Map<String, String> map = new HashMap<>();
		if(username.length() < 3 || password.length() < 3 || !password.equals(confirmPassword)) {
			map.put("error", "Invalid Credentials");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		}else {
			if(userService.existsByUsername(username)){
				map.put("error", "Username already exists");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
			}else {
				User user = userService.createNewUser(username, password);
				map.put("uuid", user.getUuid().toString());
				map.put("username", username);
				map.put("password", password);
				return ResponseEntity.status(HttpStatus.CREATED).body(map);
			}
		}
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<Map<String, String>> authenticate(@RequestBody AuthenticateDTO dto) throws JsonGenerationException, JsonMappingException, IOException {
		String username = dto.getUsername();
		String password = dto.getPassword();
		log.info("Attempting authentication {}:{}", username, password);
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		Authentication authentication= authenticationManager.authenticate(authenticationToken);
		if(authentication.isAuthenticated()) {
			org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
			User appUser = userService.loadByUsername(springUser.getUsername());
			
			Map<String, String> tokens = jwtUtil.generateTokenPair(username, appUser.getUuid(), jwtUtil.authoritiesToRoles(springUser.getAuthorities()));
			return ResponseEntity.status(HttpStatus.OK).body(tokens);	
		}else {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Invalid Credetials");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(@RequestBody String refresh_token) {
		try {
			DecodedJWT decoded = jwtUtil.verifyToken(refresh_token);
			String username = decoded.getSubject();
			List<String> roles = decoded.getClaim("roles").asList(String.class);
			UUID uuid = decoded.getClaim("uuid").as(UUID.class);
			Map<String, String> tokens = jwtUtil.generateTokenPair(username, uuid, roles);
			return ResponseEntity.status(HttpStatus.OK).body(tokens);
		} catch(JWTVerificationException e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(error);
		}	
	}
	
}
