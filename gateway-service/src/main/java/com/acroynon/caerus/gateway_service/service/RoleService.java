package com.acroynon.caerus.gateway_service.service;

import javax.persistence.EntityExistsException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acroynon.caerus.gateway_service.model.Role;
import com.acroynon.caerus.gateway_service.repo.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class RoleService {
	
	private final RoleRepository roleRepository;

	@Transactional(readOnly = false)
	public Role createNewRole(String name) throws EntityExistsException {
		if(roleRepository.existsByName(name)) {
			throw new EntityExistsException(String.format("Role (%s) already exists", name));
		}
		return roleRepository.save(new Role(null, name));
	}
	
}
