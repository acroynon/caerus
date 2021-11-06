package com.acroynon.caerus.status_service.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class StatusUpdateDTO {

	private UUID uuid;
	private String authorUsername;
	private String content;
	private int numberLikes;
	private Date creationDate;
	
}
