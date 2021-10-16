package com.acroynon.caerus.gateway_service.service;

import java.util.ArrayList;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.acroynon.caerus.gateway_service.model.Role;
import com.acroynon.caerus.gateway_service.model.User;
import com.acroynon.caerus.gateway_service.repo.RoleRepository;
import com.acroynon.caerus.gateway_service.repo.UserRepository;

@SpringBootTest
@Transactional
public class UserServiceIT {

	private UserService userService;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private static final String existingUsername = "user1";
	private static final String existingRoleName = "role1";
	
	@Autowired
	public UserServiceIT(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	@BeforeEach
	void setup() {
		User user = new User(null, existingUsername, "", new ArrayList<>());
		Role role = new Role(null, existingRoleName);
		userRepository.save(user);
		roleRepository.save(role);
	}
	
	@Test
	void test_loadUserByUsername_doesntExist() {
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			userService.loadUserByUsername("doesntExist");
		});
	}
	
	@Test
	void test_loadUserByUsername_success() {
		UserDetails user = userService.loadUserByUsername(existingUsername);
		Assertions.assertEquals(existingUsername, user.getUsername());
	}
	
	@Test
	void test_createNewUser_alreadyExists() {
		Assertions.assertThrows(EntityExistsException.class, () -> {
			userService.createNewUser(existingUsername, "");
		});
	}
	
	@Test
	void test_createNewUser_success() {
		String username = "user2";
		User user = userService.createNewUser(username, "password");
		Assertions.assertEquals(username, user.getUsername());
	}
	
	@Test
	void test_giveUserRole_userDoesntExist() {
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			userService.giveUserRole("doesntExist", existingRoleName);
		});
	}
	
	@Test
	void test_giveUserRole_roleDoesntExist() {
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			userService.giveUserRole(existingUsername, "doesntExist");
		});
	}
	
	@Test
	void test_giveUserRole_success() {
		UserDetails details = userService.loadUserByUsername(existingUsername);
		Assertions.assertEquals(0, details.getAuthorities().size());
		
		User user = userService.giveUserRole(existingUsername, existingRoleName);
		Assertions.assertEquals(1, user.getRoles().size());
		Assertions.assertEquals(existingRoleName, user.getRoles().get(0).getName());
	}
	
}
