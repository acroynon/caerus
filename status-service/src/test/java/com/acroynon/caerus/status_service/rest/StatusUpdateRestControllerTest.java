package com.acroynon.caerus.status_service.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.acroynon.caerus.security_module.util.JwtTestUtils;
import com.acroynon.caerus.status_service.dto.StatusUpdateCreationDTO;
import com.acroynon.caerus.status_service.dto.StatusUpdateDTO;
import com.acroynon.caerus.status_service.dto.UsernameUuidPair;
import com.acroynon.caerus.status_service.service.StatusUpdateService;
import com.acroynon.caerus.status_service.service.UsernameService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.net.HttpHeaders;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
class StatusUpdateRestControllerTest {

	private MockMvc mvc;
	private JsonMapper jsonMapper;
	private StatusUpdateService statusService;
	private UsernameService usernameService;
	@Mock
	private RestTemplate restTemplate;
	private UUID fakeUserUUID;
	private String fakeUsername;
	private JwtTestUtils jwtTestUtils;
	
	@Autowired
	public StatusUpdateRestControllerTest(WebApplicationContext context, StatusUpdateService statusService, UsernameService usernameService) {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
		this.statusService = statusService;
		this.usernameService = usernameService;
		jwtTestUtils = new JwtTestUtils("secret");
		jsonMapper = new JsonMapper();
	}
	
	@BeforeEach
	void mockAuthServiceResponse() {
		ReflectionTestUtils.setField(usernameService, "restTemplate", restTemplate);
		fakeUserUUID = UUID.randomUUID();
		fakeUsername = "user";
		UsernameUuidPair[] pairs = new UsernameUuidPair[1];
		pairs[0] = new UsernameUuidPair();
		pairs[0].setUuid(fakeUserUUID);
		pairs[0].setUsername(fakeUsername);
		Mockito.when(restTemplate.postForObject(
						Mockito.eq("http://auth-service/username"),
						Mockito.any(HttpEntity.class), 
						Mockito.eq(UsernameUuidPair[].class)))
				.thenReturn(pairs);
	}
	
	@Test
	void test_getStatusUpdates() throws Exception {
		// Given
		String fakeToken = jwtTestUtils.createTestToken(fakeUsername, fakeUserUUID, "user");
		StatusUpdateDTO status1 = statusService.createNew(fakeToken, "status1");
		StatusUpdateDTO status2 = statusService.createNew(fakeToken, "status2");
		StatusUpdateDTO status3 = statusService.createNew(fakeToken, "status3");
		StatusUpdateDTO status4 = statusService.createNew(fakeToken, "status4");
		StatusUpdateDTO status5 = statusService.createNew(fakeToken, "status5");
		
		// When
		MvcResult result = mvc.perform(get("/status?page=0&size=5")
					.contentType(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + fakeToken))
					.andReturn();
		TypeReference<Map<String, Object>> mapRef = new TypeReference<>() {};
		TypeReference<List<StatusUpdateDTO>> listRef = new TypeReference<>() {};
		Map<String, Object> response = jsonMapper.readValue(result.getResponse().getContentAsString(), mapRef);
		List<StatusUpdateDTO> updates = jsonMapper.readValue(jsonMapper.writeValueAsBytes(response.get("content")), listRef);
		
		// Then
		Assertions.assertEquals(5, updates.size());
		Assertions.assertTrue(updates.contains(status1));
		Assertions.assertTrue(updates.contains(status2));
		Assertions.assertTrue(updates.contains(status3));
		Assertions.assertTrue(updates.contains(status4));
		Assertions.assertTrue(updates.contains(status5));
	}
	
	@Test
	void test_postStatusUpdate() throws Exception {
		// Given 
		String content = "Hello World";
		StatusUpdateCreationDTO dto = new StatusUpdateCreationDTO();
		dto.setContent(content);
		
		// When
		MvcResult result = mvc.perform(post("/status")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtTestUtils.createTestToken(fakeUsername, fakeUserUUID, "user"))
				.content(jsonMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andReturn();
		StatusUpdateDTO update = jsonMapper.readValue(result.getResponse().getContentAsString(), StatusUpdateDTO.class);
		
		// Then
		Assertions.assertEquals(fakeUsername, update.getAuthorUsername());
		Assertions.assertEquals(content, update.getContent());
	}

}
