package com.acroynon.caerus.gateway_service.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/any")
	public ResponseEntity<String> anyPage(){
		return ResponseEntity.ok().body("Any Page");
	}
	
	@GetMapping("/user")
	public ResponseEntity<String> userPage(){
		return ResponseEntity.ok().body("User Page");
	}
	
	@GetMapping("/admin")
	public ResponseEntity<String> adminPage(){
		return ResponseEntity.ok().body("Admin Page");
	}
	
}
