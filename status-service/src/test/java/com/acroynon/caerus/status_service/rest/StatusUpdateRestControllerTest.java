package com.acroynon.caerus.status_service.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.acroynon.caerus.status_service.model.StatusUpdate;
import com.acroynon.caerus.status_service.service.StatusUpdateService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
class StatusUpdateRestControllerTest {

	private MockMvc mvc;
	private JsonMapper jsonMapper;
	private StatusUpdateService statusService;
	
	@Autowired
	public StatusUpdateRestControllerTest(WebApplicationContext context, StatusUpdateService statusService) {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
		this.statusService = statusService;
		jsonMapper = new JsonMapper();
	}
	
	@Test
	void test_getStatusUpdates() throws Exception {
		// Given
		StatusUpdate status1 = statusService.createNew(UUID.randomUUID(), "status1");
		StatusUpdate status2 = statusService.createNew(UUID.randomUUID(), "status2");
		StatusUpdate status3 = statusService.createNew(UUID.randomUUID(), "status3");
		StatusUpdate status4 = statusService.createNew(UUID.randomUUID(), "status4");
		StatusUpdate status5 = statusService.createNew(UUID.randomUUID(), "status5");
		
		// When
		MvcResult result = mvc.perform(get("/status?page=0&size=5")
				.contentType(MediaType.APPLICATION_JSON)).andReturn();
		TypeReference<Map<String, Object>> mapRef = new TypeReference<>() {};
		TypeReference<List<StatusUpdate>> listRef = new TypeReference<>() {};
		Map<String, Object> response = jsonMapper.readValue(result.getResponse().getContentAsString(), mapRef);
		List<StatusUpdate> updates = jsonMapper.readValue(jsonMapper.writeValueAsBytes(response.get("content")), listRef);
		
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
		UUID authorId = UUID.randomUUID();
		String content = "Hello World";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("author_id", authorId.toString());
		map.add("content", content);
		
		// When
		MvcResult result = mvc.perform(post("/status")
				.contentType(MediaType.APPLICATION_JSON)
				.params(map)).andExpect(status().isOk())
				.andReturn();
		StatusUpdate update = jsonMapper.readValue(result.getResponse().getContentAsString(), StatusUpdate.class);
		
		// Then
		Assertions.assertEquals(authorId, update.getAuthorGuid());
		Assertions.assertEquals(content, update.getContent());
	}

}
