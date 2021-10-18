package com.acroynon.caerus.gateway_service.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "caerus_role", schema="gateway_schema")
public class Role {

	@Id @GeneratedValue
	private UUID uuid;
	
	private String name;
	
}
