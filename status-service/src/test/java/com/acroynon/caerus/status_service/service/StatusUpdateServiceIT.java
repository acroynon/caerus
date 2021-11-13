package com.acroynon.caerus.status_service.service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.acroynon.caerus.status_service.dto.StatusUpdateDTO;
import com.acroynon.caerus.status_service.dto.UsernameUuidPair;
import com.acroynon.caerus.status_service.model.StatusUpdate;
import com.acroynon.caerus.status_service.repo.StatusUpdateRepository;

@SpringBootTest
@Transactional
class StatusUpdateServiceIT {
	
	@Value("${status.maximum-length}")
	private int MAXIMUM_LENGTH;

	private StatusUpdateService statusService;
	private UsernameService usernameService;
	private StatusUpdateRepository statusRepo;
	@Mock
	private RestTemplate restTemplate;
	
	@Autowired
	public StatusUpdateServiceIT(StatusUpdateService statusService, UsernameService usernameService, StatusUpdateRepository statusRepo) {
		this.statusService = statusService;
		this.usernameService = usernameService;
		this.statusRepo = statusRepo;		
	}
	
	@BeforeEach
	void mockAuthServiceResponse() {
		ReflectionTestUtils.setField(usernameService, "restTemplate", restTemplate);
		UsernameUuidPair[] pairs = new UsernameUuidPair[1];
		pairs[0] = new UsernameUuidPair();
		pairs[0].setUuid(UUID.randomUUID());
		pairs[0].setUsername("");
		Mockito.when(restTemplate.postForObject(
						Mockito.eq("http://auth-service/username"),
						Mockito.any(HttpEntity.class), 
						Mockito.eq(UsernameUuidPair[].class)))
				.thenReturn(pairs);
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
		Page<StatusUpdateDTO> page1 = statusService.findAll("", PageRequest.of(0, 1));
		Page<StatusUpdateDTO> page2 = statusService.findAll("", PageRequest.of(0, 3));
		Page<StatusUpdateDTO> page3 = statusService.findAll("", PageRequest.of(0, 5));
		
		// Then
		statusEqualsDTO(status5, page1.getContent().get(0));
		Assertions.assertEquals(1, page1.getSize());
		Assertions.assertEquals(5, page1.getTotalElements());
		Assertions.assertEquals(5, page1.getTotalPages());
		
		statusEqualsDTO(status5, page2.getContent().get(0));
		statusEqualsDTO(status4, page2.getContent().get(1));
		statusEqualsDTO(status3, page2.getContent().get(2));
		Assertions.assertEquals(3, page2.getSize());
		Assertions.assertEquals(5, page2.getTotalElements());
		Assertions.assertEquals(2, page2.getTotalPages());		
		
		statusEqualsDTO(status5, page3.getContent().get(0));
		statusEqualsDTO(status4, page3.getContent().get(1));
		statusEqualsDTO(status3, page3.getContent().get(2));
		statusEqualsDTO(status2, page3.getContent().get(3));
		statusEqualsDTO(status1, page3.getContent().get(4));
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
		Page<StatusUpdateDTO> page1 = statusService.findAll("", PageRequest.of(0, 2));
		Page<StatusUpdateDTO> page2 = statusService.findAll("", PageRequest.of(1, 2));
		Page<StatusUpdateDTO> page3 = statusService.findAll("", PageRequest.of(2, 2));
		
		// Then
		statusEqualsDTO(status5, page1.getContent().get(0));
		statusEqualsDTO(status4, page1.getContent().get(1));
		statusEqualsDTO(status3, page2.getContent().get(0));
		statusEqualsDTO(status2, page2.getContent().get(1));
		statusEqualsDTO(status1, page3.getContent().get(0));
		Assertions.assertEquals(1, page3.getNumberOfElements());
	}
	
	private void statusEqualsDTO(StatusUpdate entity, StatusUpdateDTO dto) {
		Assertions.assertEquals(entity.getContent(), dto.getContent());
		Assertions.assertEquals(entity.getUuid(), dto.getUuid());
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
