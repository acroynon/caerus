package com.acroynon.caerus.gateway_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RegisterDTO {

	private String username;
	private String password;
	@JsonProperty("confirm_password")
	private String confirmPassword;
	
}
