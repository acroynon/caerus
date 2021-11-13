package com.acroynon.caerus.status_service.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "status_update", schema="status_schema")
public class StatusUpdate {

	@Id @GeneratedValue
	private UUID uuid;
	
	private UUID authorGuid;
	
	private String content;
	
	private int numberLikes;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
}
