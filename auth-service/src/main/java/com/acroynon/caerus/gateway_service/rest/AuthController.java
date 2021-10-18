package com.acroynon.caerus.gateway_service.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acroynon.caerus.gateway_service.util.JwtUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController @RequiredArgsConstructor @Slf4j
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	@PostMapping("/register")
	public void register() {
		
	}
	
	@PostMapping("/authenticate")
	public void authenticate(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		log.info("Attempting authentication {}:{}", username, password);
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		Authentication authentication= authenticationManager.authenticate(authenticationToken);
		if(authentication.isAuthenticated()) {
			User user = (User) authentication.getPrincipal();
			String issuer = request.getRequestURI().toString();
			String accessToken = jwtUtil.createToken(user.getUsername(), issuer, jwtUtil.authoritiesToRoles(user.getAuthorities()), 10);
			String refreshToken = jwtUtil.createToken(user.getUsername(), issuer, jwtUtil.authoritiesToRoles(user.getAuthorities()), 30);
			Map<String, String> tokens = new HashMap<>();
			tokens.put("access_token", accessToken);
			tokens.put("refresh_token", refreshToken);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			new ObjectMapper().writeValue(response.getOutputStream(), tokens);	
		}else {
			response.setHeader("error", "Invalid Credentials");
			response.setStatus(HttpStatus.FORBIDDEN.value());
			Map<String, String> error = new HashMap<>();
			error.put("error", "Invalid Credetials");
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			new ObjectMapper().writeValue(response.getOutputStream(), error);
		}
	}
	
	@PostMapping("/refresh")
	public void refresh() {
		
	}
	
}
