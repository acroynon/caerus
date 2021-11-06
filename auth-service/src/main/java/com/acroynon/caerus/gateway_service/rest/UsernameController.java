package com.acroynon.caerus.gateway_service.rest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acroynon.caerus.gateway_service.dto.UsernameUuidPair;
import com.acroynon.caerus.gateway_service.model.User;
import com.acroynon.caerus.gateway_service.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor
public class UsernameController {

	private final UserService userService;
	
	@PostMapping("/username")
	public List<UsernameUuidPair> getUsernamesFromUuids(@RequestBody List<UUID> userUuidList){
		List<User> users = userService.findByUuidIn(userUuidList);
		List<UsernameUuidPair> pairs = users.stream().map(user -> {
			return new UsernameUuidPair(user.getUuid(), user.getUsername());
		}).collect(Collectors.toList());
		return pairs;		
	}
	
}
