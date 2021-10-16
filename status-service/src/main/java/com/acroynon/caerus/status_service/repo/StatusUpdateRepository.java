package com.acroynon.caerus.status_service.repo;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.acroynon.caerus.status_service.model.StatusUpdate;

@Repository
public interface StatusUpdateRepository extends PagingAndSortingRepository<StatusUpdate, UUID>{

	Page<StatusUpdate> findAll(Pageable pageable);
	
	void deleteById(UUID id);
	
	
}
