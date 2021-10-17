package com.acroynon.caerus.gateway_service.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acroynon.caerus.gateway_service.model.Role;
import com.acroynon.caerus.gateway_service.model.User;
import com.acroynon.caerus.gateway_service.repo.RoleRepository;
import com.acroynon.caerus.gateway_service.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Override @Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> {
			return new UsernameNotFoundException("Username not found");
		});
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				authorities);
	}
	
	@Transactional(readOnly = true)
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Transactional(readOnly = false)
	public User createNewUser(String username, String password) throws EntityExistsException {
		if (userRepository.existsByUsername(username)) {
			throw new EntityExistsException(String.format("Username (%s) already exists ", username));
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		return userRepository.save(user);
	}

	@Transactional(readOnly = false)
	public User giveUserRole(String username, String roleName) throws EntityNotFoundException {
		Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Role (%s) not found", roleName)));
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Username (%s) not found", username)));
		if(!user.getRoles().contains(role)){
			user.getRoles().add(role);	
		}
		return userRepository.save(user);
	}

}
