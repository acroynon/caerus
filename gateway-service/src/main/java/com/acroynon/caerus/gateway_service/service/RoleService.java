package com.acroynon.caerus.gateway_service.service;

import org.springframework.stereotype.Service;

import com.acroynon.caerus.gateway_service.model.Role;
import com.acroynon.caerus.gateway_service.repo.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class RoleService {
	
	private final RoleRepository roleRepository;

	public Role createNewRole(String name) {
		return roleRepository.save(new Role(null, name));
	}
	
}
