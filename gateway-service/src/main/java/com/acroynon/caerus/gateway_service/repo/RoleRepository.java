package com.acroynon.caerus.gateway_service.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.acroynon.caerus.gateway_service.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, UUID> {

	Optional<Role> findByName(String name);	
	
}
