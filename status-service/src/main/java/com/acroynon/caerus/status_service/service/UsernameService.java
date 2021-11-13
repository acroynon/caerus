package com.acroynon.caerus.status_service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.acroynon.caerus.status_service.dto.UsernameUuidPair;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class UsernameService {
	
	private final RestTemplate restTemplate;
	
	public Map<UUID, String> getUsernameUuidMap(String token, List<UUID> uuids){
		Map<UUID, String> usernameMap = new HashMap<>();
		HttpHeaders headers = new HttpHeaders() {
			private static final long serialVersionUID = 583050783803710144L;
			{
		        set("Authorization", "Bearer " + token); 
		    }
		};
	    HttpEntity<List<UUID>> httpEntity = new HttpEntity<List<UUID>>(uuids, headers);
		UsernameUuidPair[] pairs = restTemplate
				.postForObject("http://auth-service/username",
						httpEntity, 
						UsernameUuidPair[].class);
		System.out.println(pairs);
		for(UsernameUuidPair pair : pairs) {
			usernameMap.put(pair.getUuid(), pair.getUsername());
		}
		return usernameMap;
	}
	
}
