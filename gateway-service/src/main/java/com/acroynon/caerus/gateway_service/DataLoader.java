package com.acroynon.caerus.gateway_service;

import javax.annotation.PostConstruct;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.acroynon.caerus.gateway_service.service.RoleService;
import com.acroynon.caerus.gateway_service.service.UserService;

import lombok.RequiredArgsConstructor;

@Component @RequiredArgsConstructor
public class DataLoader {

	private final UserService userService;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void setupTestData() {
		roleService.createNewRole("ROLE_USER");
		roleService.createNewRole("ROLE_ADMIN");
		
		userService.createNewUser("adam", passwordEncoder.encode("123"));
		userService.createNewUser("bob", passwordEncoder.encode("456"));
		userService.createNewUser("charlie", passwordEncoder.encode("789"));
		
		userService.giveUserRole("adam", "ROLE_USER");
		userService.giveUserRole("adam", "ROLE_ADMIN");
		userService.giveUserRole("bob", "ROLE_USER");
		userService.giveUserRole("charlie", "ROLE_ADMIN");
		
		
	}
	
}
