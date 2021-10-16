package com.acroynon.caerus.status_service.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class StatusUpdate {

	@Id @GeneratedValue
	private UUID guid;
	
	private UUID authorGuid;
	
	private String content;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
}
