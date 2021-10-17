package com.acroynon.caerus.gateway_service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.acroynon.caerus.gateway_service.service.RoleService;
import com.acroynon.caerus.gateway_service.service.UserService;

import lombok.RequiredArgsConstructor;

@Component @RequiredArgsConstructor
public class DataLoader {

	private final UserService userService;
	private final RoleService roleService;
	
	@PostConstruct
	public void setupTestData() {
		createRoleIfNotExists("ROLE_USER");
		createRoleIfNotExists("ROLE_ADMIN");
		
		if(!userService.existsByUsername("adam")) {
			userService.createNewUser("adam", "123");
			userService.giveUserRole("adam", "ROLE_USER");
			userService.giveUserRole("adam", "ROLE_ADMIN");
		}
		
		if(!userService.existsByUsername("bob")) {
			userService.createNewUser("bob", "456");
			userService.giveUserRole("bob", "ROLE_USER");			
		}
		
		if(!userService.existsByUsername("charlie")) {
			userService.createNewUser("charlie", "789");
			userService.giveUserRole("charlie", "ROLE_ADMIN");			
		}
		
	}
	
	private void createRoleIfNotExists(String name) {
		if(!roleService.existsByName(name)) {
			roleService.createNewRole(name);
		}
	}
	
}
