package com.acroynon.caerus.status_service.service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.acroynon.caerus.status_service.model.StatusUpdate;
import com.acroynon.caerus.status_service.repo.StatusUpdateRepository;

@SpringBootTest
@Transactional
class StatusUpdateServiceIT {
	
	@Value("${status.maximum-length}")
	private int MAXIMUM_LENGTH;

	private StatusUpdateService statusService;
	private StatusUpdateRepository statusRepo;
	
	@Autowired
	public StatusUpdateServiceIT(StatusUpdateService statusService, StatusUpdateRepository statusRepo) {
		this.statusService = statusService;
		this.statusRepo = statusRepo;
	}
	
	@Test
	void test_findAll_size() {
		// Given
		StatusUpdate status1 = createStatusUpdate(UUID.randomUUID(), "Status1", 0);
		StatusUpdate status2 = createStatusUpdate(UUID.randomUUID(), "Status2", 1);
		StatusUpdate status3 = createStatusUpdate(UUID.randomUUID(), "Status3", 2);
		StatusUpdate status4 = createStatusUpdate(UUID.randomUUID(), "Status4", 3);
		StatusUpdate status5 = createStatusUpdate(UUID.randomUUID(), "Status5", 4);
		
		// When
		Page<StatusUpdate> page1 = statusService.findAll(PageRequest.of(0, 1));
		Page<StatusUpdate> page2 = statusService.findAll(PageRequest.of(0, 3));
		Page<StatusUpdate> page3 = statusService.findAll(PageRequest.of(0, 5));
		
		// Then
		Assertions.assertEquals(status5, page1.getContent().get(0));
		Assertions.assertEquals(1, page1.getSize());
		Assertions.assertEquals(5, page1.getTotalElements());
		Assertions.assertEquals(5, page1.getTotalPages());
		
		Assertions.assertEquals(status5, page2.getContent().get(0));
		Assertions.assertEquals(status4, page2.getContent().get(1));
		Assertions.assertEquals(status3, page2.getContent().get(2));
		Assertions.assertEquals(3, page2.getSize());
		Assertions.assertEquals(5, page2.getTotalElements());
		Assertions.assertEquals(2, page2.getTotalPages());		
		
		Assertions.assertEquals(status5, page3.getContent().get(0));
		Assertions.assertEquals(status4, page3.getContent().get(1));
		Assertions.assertEquals(status3, page3.getContent().get(2));
		Assertions.assertEquals(status2, page3.getContent().get(3));
		Assertions.assertEquals(status1, page3.getContent().get(4));
		Assertions.assertEquals(5, page3.getSize());
		Assertions.assertEquals(5, page3.getTotalElements());
		Assertions.assertEquals(1, page3.getTotalPages());	
	}
	
	@Test
	void test_findAll_page() {
		// Given
		StatusUpdate status1 = createStatusUpdate(UUID.randomUUID(), "Status1", 0);
		StatusUpdate status2 = createStatusUpdate(UUID.randomUUID(), "Status2", 1);
		StatusUpdate status3 = createStatusUpdate(UUID.randomUUID(), "Status3", 2);
		StatusUpdate status4 = createStatusUpdate(UUID.randomUUID(), "Status4", 3);
		StatusUpdate status5 = createStatusUpdate(UUID.randomUUID(), "Status5", 4);
		
		// When
		Page<StatusUpdate> page1 = statusService.findAll(PageRequest.of(0, 2));
		Page<StatusUpdate> page2 = statusService.findAll(PageRequest.of(1, 2));
		Page<StatusUpdate> page3 = statusService.findAll(PageRequest.of(2, 2));
		
		// Then
		Assertions.assertEquals(status5, page1.getContent().get(0));
		Assertions.assertEquals(status4, page1.getContent().get(1));
		Assertions.assertEquals(status3, page2.getContent().get(0));
		Assertions.assertEquals(status2, page2.getContent().get(1));
		Assertions.assertEquals(status1, page3.getContent().get(0));
		Assertions.assertEquals(1, page3.getNumberOfElements());
	}
	
	private StatusUpdate createStatusUpdate(UUID author_id, String content, int minsAdd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, minsAdd);
		StatusUpdate status = new StatusUpdate();
		status.setAuthorGuid(author_id);
		status.setContent(content);
		status.setCreationDate(cal.getTime());
		return statusRepo.save(status);
	}

}
