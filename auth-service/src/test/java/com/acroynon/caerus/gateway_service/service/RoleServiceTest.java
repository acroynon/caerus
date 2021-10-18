package com.acroynon.caerus.gateway_service.service;

import javax.persistence.EntityExistsException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.acroynon.caerus.gateway_service.model.Role;
import com.acroynon.caerus.gateway_service.repo.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	private RoleService roleService;
	
	@Test
	void test_createNewRole_alreadyExists() {
		// Given
		String roleName = "role";
		Mockito.when(roleRepository.existsByName(roleName)).thenReturn(true);
		
		// When
		Assertions.assertThrows(EntityExistsException.class, () -> {
			roleService.createNewRole(roleName);
		});
		
		// Then
		Mockito.verify(roleRepository, Mockito.times(1)).existsByName(Mockito.eq(roleName));
		Mockito.verifyNoMoreInteractions(roleRepository);
	}
	
	@Test
	void test_createNewRole_success() {
		// Given
		String roleName = "role";
		Role expected = new Role(null, roleName);
		Mockito.when(roleRepository.save(expected)).thenReturn(expected);
		
		// When
		Role actual = roleService.createNewRole(roleName);
		
		// Then
		Assertions.assertNotNull(actual);
		Assertions.assertEquals(roleName, actual.getName());
		Mockito.verify(roleRepository, Mockito.times(1)).existsByName(Mockito.eq(roleName));
		Mockito.verify(roleRepository, Mockito.times(1)).save(Mockito.eq(expected));
		Mockito.verifyNoMoreInteractions(roleRepository);
	}

}
