package com.acroynon.caerus.status_service.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acroynon.caerus.status_service.dto.StatusUpdateCreationDTO;
import com.acroynon.caerus.status_service.dto.StatusUpdateDTO;
import com.acroynon.caerus.status_service.service.StatusUpdateService;
import com.google.common.net.HttpHeaders;

import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor
public class StatusUpdateRestController {

	private final StatusUpdateService statusService;
	
	@GetMapping("/status")
	public ResponseEntity<Page<StatusUpdateDTO>> getStatusUpdates(
				@RequestHeader (name=HttpHeaders.AUTHORIZATION) String token,
				@RequestParam(defaultValue = "0") int page,
				@RequestParam(defaultValue = "10") int size){
		Pageable paging = PageRequest.of(page, size);		
		return ResponseEntity.ok(statusService.findAll(token, paging));
	}
	
	@PostMapping("/status")
	public ResponseEntity<?> postStatusUpdate(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody StatusUpdateCreationDTO dto){
		try {
			StatusUpdateDTO update = statusService.createNew(token, dto.getContent());
			return ResponseEntity.ok(update);
		}catch(IllegalArgumentException e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
}
