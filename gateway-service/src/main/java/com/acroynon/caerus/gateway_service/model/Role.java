package com.acroynon.caerus.gateway_service.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Role {

	@Id @GeneratedValue
	private UUID uuid;
	
	private String name;
	
}
