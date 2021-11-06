package com.acroynon.caerus.status_service.dto;

import java.util.UUID;

import lombok.Data;

@Data 
public class UsernameUuidPair {

	private UUID uuid;
	private String username;
	
}
