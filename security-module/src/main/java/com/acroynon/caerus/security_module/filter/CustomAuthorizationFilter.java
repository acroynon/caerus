package com.acroynon.caerus.security_module.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.acroynon.caerus.security_module.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

	private final List<String> unauthorisedPaths;	
	private final JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(unauthorisedPaths.contains(request.getServletPath())) {
			filterChain.doFilter(request, response);
		}else {
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try {
					String token = authorizationHeader.substring("Bearer ".length());					
					DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
					String username = decodedJWT.getSubject();
					Collection<SimpleGrantedAuthority> authorities = jwtUtil.authoritiesFromToken(decodedJWT);
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
				}catch(Exception e) {
					log.error("Error logging in : {}", e.getMessage());
					response.setHeader("error", e.getMessage());
					response.setStatus(HttpStatus.FORBIDDEN.value());
					Map<String, String> error = new HashMap<>();
					error.put("error", e.getMessage());
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(), error);
					
				}
			}else {
				filterChain.doFilter(request, response);
			}
		}
	}

}
