package com.acroynon.caerus.gateway_service.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class UsernameUuidPair {

	private UUID uuid;
	private String username;
	
}
