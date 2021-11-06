package com.acroynon.caerus.gateway_service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UserUuidListDTO {

	private List<UUID> uuids = new ArrayList<>();
	
}
