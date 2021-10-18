package com.acroynon.caerus.gateway_service.service;

import javax.persistence.EntityExistsException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.acroynon.caerus.gateway_service.model.Role;
import com.acroynon.caerus.gateway_service.repo.RoleRepository;

@SpringBootTest 
@Transactional
public class RoleServiceIT {

	private final RoleService roleService;
	private final RoleRepository roleRepository;
	
	@Autowired
	public RoleServiceIT(RoleService roleService, RoleRepository roleRepository) {
		this.roleService = roleService;
		this.roleRepository = roleRepository;
	}
	
	@Test
	void test_createNewRole_alreadyExists() {
		// Given
		String roleName = "USER";
		Role role = new Role(null, roleName);
		roleRepository.save(role);
		
		// When/Then
		Assertions.assertThrows(EntityExistsException.class, () -> {
			roleService.createNewRole(roleName);
		});		
	}
	
	@Test
	void test_createNewRole_success() {
		// Given
		String roleName = "ADMIN";
		
		// When
		Role role = roleService.createNewRole(roleName);
		
		// Then
		Assertions.assertNotNull(role);
		Assertions.assertEquals(roleName, role.getName());
		Assertions.assertTrue(roleRepository.existsByName(roleName));
	}
	
}
