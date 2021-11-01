package com.acroynon.caerus.status_service.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.acroynon.caerus.status_service.model.StatusUpdate;
import com.acroynon.caerus.status_service.repo.StatusUpdateRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class StatusUpdateService {

	@Value("${status.maximum-length}")
	private int MAXIMUM_LENGTH;
	
	private final StatusUpdateRepository statusRepo;
	
	public Page<StatusUpdate> findAll(Pageable pageable){
		return statusRepo.findAllByOrderByCreationDateDesc(pageable);
	}
	
	public StatusUpdate createNew(UUID author_id, String content) throws IllegalArgumentException {
		if(content.length() > MAXIMUM_LENGTH) {
			throw new IllegalArgumentException(String.format("Status is too long. Must be up to %d characters", MAXIMUM_LENGTH));
		}
		StatusUpdate status = new StatusUpdate();
		status.setAuthorGuid(author_id);
		status.setContent(content);
		status.setCreationDate(new Date());
		return statusRepo.save(status);
	}
	
	public void deleteStatus(UUID id) {
		statusRepo.deleteById(id);
	}
	
}
