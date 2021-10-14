package com.acroynon.caerus.gateway_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.acroynon.caerus.gateway_service.model.Role;
import com.acroynon.caerus.gateway_service.model.User;
import com.acroynon.caerus.gateway_service.repo.RoleRepository;
import com.acroynon.caerus.gateway_service.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service @RequiredArgsConstructor
public class UserService implements UserDetailsService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> {
			log.error("Username not found: {}", username);
			return new UsernameNotFoundException("Username not found");
		});
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new org.springframework.security.core.userdetails.User(user.getPassword(), user.getPassword(), authorities);
	}
	
	public User createNewUser(String username, String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		return userRepository.save(user);
	}
	
	public User giveUserRole(String username, String roleName) {
		// TODO: Change to better exception handling/thrown
		Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException());
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException());
		user.getRoles().add(role);
		return userRepository.save(user);
	}
	
}
