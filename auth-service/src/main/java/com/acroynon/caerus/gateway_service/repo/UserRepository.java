package com.acroynon.caerus.gateway_service.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.acroynon.caerus.gateway_service.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
	List<User> findByUuidIn(List<UUID> uuids);
	
}
