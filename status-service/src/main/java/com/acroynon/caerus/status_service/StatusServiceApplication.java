package com.acroynon.caerus.status_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.acroynon.caerus.security_module.util.JwtUtil;

@SpringBootApplication
@EnableDiscoveryClient
public class StatusServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatusServiceApplication.class, args);
	}
	
	@Bean
	public JwtUtil jwtUtil(@Value("${jwt.secret}") String secret) {
		return new JwtUtil(secret);
	}
	
	@Bean @LoadBalanced
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

}
