package com.acroynon.caerus.gateway_service.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.acroynon.caerus.gateway_service.model.Role;
import com.acroynon.caerus.gateway_service.model.User;
import com.acroynon.caerus.gateway_service.repo.RoleRepository;
import com.acroynon.caerus.gateway_service.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	private UserService userService;

	@DisplayName("loadUserByUsername: Username doesn't exist")
	@Test
	void test_loadUserByUsername_notFound() {
		// Given
		String username = "doesntExist";

		// When
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			userService.loadUserByUsername(username);
		});
		
		// Then
		Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.eq(username));
		verifyNoMoreInteractions();
	}

	@DisplayName("loadUserByUsername: user exists")
	@Test
	void test_loadUserByUsername_userExists() {
		// Given
		String username = "user";
		String password = "password";
		List<String> roleNames = Arrays.asList("USER", "ADMIN");
		List<Role> roles = new ArrayList<>();
		roleNames.forEach(name -> roles.add(new Role(null, name)));
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		roleNames.forEach(name -> authorities.add(new SimpleGrantedAuthority(name)));
		User user = new User(null, username, password, roles);
		Mockito.when(userRepository.findByUsername(Mockito.eq(username))).thenReturn(Optional.of(user));

		// When
		UserDetails details = userService.loadUserByUsername(username);

		// Then
		Assertions.assertNotNull(details);
		Assertions.assertEquals(username, details.getUsername());
		Assertions.assertEquals(password, details.getPassword());
		Assertions.assertEquals(authorities.size(), details.getAuthorities().size());
		for (SimpleGrantedAuthority authority : authorities) {
			Assertions.assertTrue(details.getAuthorities().contains(authority));
		}
		Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.eq(username));
		verifyNoMoreInteractions();
	}

	@DisplayName("createNewUser: duplicate username")
	@Test
	void test_createNewUser_duplicateUsername() {
		// Given
		String username = "username";
		Mockito.when(userRepository.existsByUsername(Mockito.eq(username))).thenReturn(true);

		// When
		Assertions.assertThrows(EntityExistsException.class, () -> {
			userService.createNewUser(username, "password");
		});
		
		// Then
		Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(Mockito.eq(username));
		verifyNoMoreInteractions();
	}

	@DisplayName("createNewUser: success")
	@Test
	void test_createNewUser_success() {
		// Given
		String username = "username";
		String password = "password";
		User expected = new User(null, username, password, new ArrayList<>());
		Mockito.when(userRepository.existsByUsername(Mockito.eq(username))).thenReturn(false);
		Mockito.when(userRepository.save(Mockito.eq(expected))).thenReturn(expected);

		// When
		User actual = userService.createNewUser(username, password);
			
		// Then
		Assertions.assertNotNull(actual);
		Assertions.assertEquals(expected, actual);
		Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(Mockito.eq(username));
		Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.eq(expected));
		verifyNoMoreInteractions();
	}

	@DisplayName("giveUserRole: user doesn't exist")
	@Test
	void test_giveUserRole_userNotExist() {
		// Given
		String username = "username";
		String roleName = "role";
		Mockito.when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());
		
		// When
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			userService.giveUserRole(username, roleName);
		});
		
		// Then
		Mockito.verify(roleRepository, Mockito.times(1)).findByName(Mockito.eq(roleName));
		verifyNoMoreInteractions();		
	}

	@DisplayName("giveUserRole: role doesn't exist")
	@Test
	void test_giveUserRole_roleNotExist() {
		// Given
		String username = "username";
		String roleName = "role";
		Role role = new Role(null, roleName);
		Mockito.when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
		Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
		
		// When
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			userService.giveUserRole(username, roleName);
		});
		
		// Then
		Mockito.verify(roleRepository, Mockito.times(1)).findByName(Mockito.eq(roleName));
		Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.eq(username));
		verifyNoMoreInteractions();	
	}

	@DisplayName("giveUserRole: success")
	@Test
	void test_giveUserRole_success() {
		// Given
		String username = "username";
		String roleName = "role";
		User user = new User();
		user.setUsername(username);
		Role role = new Role(null, roleName);
		Mockito.when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
		Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		Mockito.when(userRepository.save(user)).thenReturn(user);
		
		
		// When
		User actual = userService.giveUserRole(username, roleName);
		
		// Then
		Assertions.assertNotNull(actual);
		Assertions.assertTrue(actual.getRoles().size() == 1);
		Assertions.assertTrue(actual.getRoles().contains(role));
		Mockito.verify(roleRepository, Mockito.times(1)).findByName(Mockito.eq(roleName));
		Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.eq(username));
		Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.eq(user));
		verifyNoMoreInteractions();	
	}

	private void verifyNoMoreInteractions() {
		Mockito.verifyNoMoreInteractions(userRepository);
		Mockito.verifyNoMoreInteractions(roleRepository);
	}

}
