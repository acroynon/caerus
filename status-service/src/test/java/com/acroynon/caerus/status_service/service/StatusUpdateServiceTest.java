package com.acroynon.caerus.status_service.service;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.acroynon.caerus.status_service.dto.StatusUpdateDTO;
import com.acroynon.caerus.status_service.model.StatusUpdate;
import com.acroynon.caerus.status_service.repo.StatusUpdateRepository;

@ExtendWith(MockitoExtension.class)
class StatusUpdateServiceTest {
	
	private int MAXIMUM_LENGTH = 120;
	
	@Mock
	private StatusUpdateRepository statusRepo;
	
	@InjectMocks
	private StatusUpdateService statusService;
	
	@BeforeEach
	void setup() {
		ReflectionTestUtils.setField(statusService, "MAXIMUM_LENGTH", MAXIMUM_LENGTH);
	}
	
	@Test
	void test_findAll() {
		// Given
		Pageable pageable = Pageable.ofSize(1);
		Page<StatusUpdate> expected = new PageImpl<StatusUpdate>(new ArrayList<>());
		Mockito.when(statusRepo.findAllByOrderByCreationDateDesc(pageable)).thenReturn(expected);
		
		// When
		Page<StatusUpdateDTO> actual = statusService.findAll("", pageable);
		
		// Then
		Assertions.assertNotNull(actual);
		Assertions.assertEquals(expected, actual);
		Mockito.verify(statusRepo, Mockito.times(1)).findAllByOrderByCreationDateDesc(Mockito.eq(pageable));
		Mockito.verifyNoMoreInteractions(statusRepo);		
	}
	
	@Test
	void test_createNew_contentTooLong() {
		// Given
		String content = "0".repeat(MAXIMUM_LENGTH+1);
		
		// When
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			statusService.createNew(null, content);
		});
		
		// Then
		Mockito.verifyNoMoreInteractions(statusRepo);				
	}
	
	@Test
	void test_createNew_contentMaximumLength() {
		// Given
		String content = "0".repeat(MAXIMUM_LENGTH);
		
		// When
		statusService.createNew(null, content);
		
		// Then
		Mockito.verify(statusRepo, Mockito.times(1)).save(Mockito.any(StatusUpdate.class));
		Mockito.verifyNoMoreInteractions(statusRepo);
	}
	
	@Test
	void test_createNew_basicSuccess() {
		// Given
		String content = "Hello World";
		
		// When
		statusService.createNew(null, content);
		
		// Then
		Mockito.verify(statusRepo, Mockito.times(1)).save(Mockito.any(StatusUpdate.class));
		Mockito.verifyNoMoreInteractions(statusRepo);
	}
	
	@Test
	void test_deleteStatus() {
		// Given
		UUID uuid = UUID.randomUUID();
		
		// When
		statusService.deleteStatus(uuid);
		
		// Then
		Mockito.verify(statusRepo, Mockito.times(1)).deleteById(Mockito.eq(uuid));
		Mockito.verifyNoMoreInteractions(statusRepo);
		
	}

}
