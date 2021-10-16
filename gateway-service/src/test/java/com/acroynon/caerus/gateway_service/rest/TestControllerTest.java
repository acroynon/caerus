package com.acroynon.caerus.gateway_service.rest;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.acroynon.caerus.gateway_service.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
class TestControllerTest {

	@Value("${jwt.secret")
	private String secret;

	private UserService userService;
	private MockMvc mvc;
	private JsonMapper jsonMapper;
	private String username_userRole = "user1";
	private String username_adminRole = "user2";
	private String username_noRole = "user3";
	private String password = "password";

	@Autowired
	public TestControllerTest(WebApplicationContext context, UserService userService) {
		this.userService = userService;
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		jsonMapper = new JsonMapper();
	}

	@BeforeEach
	void setup() {
		userService.createNewUser(username_userRole, password);
		userService.createNewUser(username_adminRole, password);
		userService.createNewUser(username_noRole, password);
		// These roles already exist in the system
		userService.giveUserRole(username_userRole, "ROLE_USER");
		userService.giveUserRole(username_adminRole, "ROLE_ADMIN");
	}

	@Test
	void test_unsecuredPage_noAuthentication() throws Exception {
		mvc.perform(get("/any")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

	@Test
	void test_unsecuredPage_Authenticated() throws Exception {
		String token = createToken(username_userRole);
		mvc.perform(get("/any")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

	@Test
	void test_userPage_noAuthentication() throws Exception {
		mvc.perform(get("/user")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	void test_userPage_AuthenticationSuccess() throws Exception {
		String token = createToken(username_userRole);
		mvc.perform(get("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

	@Test
	void test_userPage_missingRole() throws Exception {
		String token = createToken(username_noRole);
		mvc.perform(get("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isForbidden());
	}

	@Test
	void test_adminPage_noAuthentication() throws Exception{
		mvc.perform(get("/admin")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden());
	}
	
	@Test
	void test_adminPage_AuthenticationSuccess() throws Exception {
		String token = createToken(username_adminRole);
		mvc.perform(get("/admin")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());		
	}

	@Test
	void test_adminPage_missingRole() throws Exception {
		String token = createToken(username_userRole);
		mvc.perform(get("/admin")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isForbidden());		
	}

	private String createToken(String username) throws Exception {
		MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
		request.add("username", username);
		request.add("password", "password");
		MvcResult result = mvc
				.perform(post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED).params(request)).andReturn();
		TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
		};
		Map<String, String> response = jsonMapper.readValue(result.getResponse().getContentAsString(), typeRef);
		return response.get("access_token");
	}

}
