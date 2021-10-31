package com.acroynon.caerus.gateway_service.filter;

import java.util.Date;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.acroynon.caerus.gateway_service.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component @RefreshScope @RequiredArgsConstructor
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	private final JwtUtil jwtUtil;

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			
			if(!isAuthMissing(request)) {
				return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
			}
			
			final String token = this.getAuthHeader(request);
			DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
			if(decodedJWT.getIssuedAt().before(new Date())) {
				return onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
			}
			
			String[] roles = jwtUtil.getRolesFromToken(decodedJWT);
			for(String configRole : config.getRoles()) {
				for(String userRole : roles) {
					if(userRole.equals(configRole)) {
						populateRequestWithRoles(exchange, roles);
						return chain.filter(exchange);
					}
				}
			}
			return this.onError(exchange, "Required role is missing", HttpStatus.UNAUTHORIZED);
		});
	}
	
	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}
	
	private String getAuthHeader(ServerHttpRequest request) {
		return request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
	}
	
	private boolean isAuthMissing(ServerHttpRequest request) {
		return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
	}
	
	private void populateRequestWithRoles(ServerWebExchange exchange, String[] roles) {
		exchange.getRequest().mutate()
			.header("roles", roles)
			.build();
	}
	
	@Getter
	@RequiredArgsConstructor
	public class Config {
		private final String[] roles;
	}

}
