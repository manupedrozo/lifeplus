package com.lifeplus.lifeplus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifeplus.lifeplus.model.form.LoginForm;
import com.lifeplus.lifeplus.model.User;
import com.lifeplus.lifeplus.model.UserType;
import com.lifeplus.lifeplus.repository.UserRepository;
import com.lifeplus.lifeplus.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuthControllerTest extends AbstractTest {

	@Autowired
	UserRepository userRepository;

	@Before
	public void setUpDb() {
	    User user1 = new User("User1", "Last1", "username1", "1111111", "user1@gmail.com", BCrypt.hashpw("pass1", BCrypt.gensalt()), true, UserType.USER);
		User user2 = new User("User2", "Last2", "username2", "2222222","user2@gmail.com", BCrypt.hashpw("pass2", BCrypt.gensalt()), true, UserType.USER);
		userRepository.save(user1);
		userRepository.save(user2);
	}

	@After
	public void rollBackDb() {
	    userRepository.deleteAll();
	}

	@Test
	public void LogInWithCorrectDataShouldReturnToken() throws Exception {
		String uri = "/auth";
		LoginForm loginForm = new LoginForm("username1", "pass1");

		MvcResult loginResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(loginForm))).andReturn();

		int status = loginResult.getResponse().getStatus();
		assertEquals(status, 200);

		ObjectMapper mapper = new ObjectMapper();
		String loginContent = loginResult.getResponse().getContentAsString();
		JsonNode tokenJson = mapper.readTree(loginContent);

		assertTrue(tokenJson.get("token").toString().length() > 0);
	}

	@Test
	public void LogInWithIncorrectPasswordShouldReturnUnauthorized() throws Exception {
		String uri = "/auth";
		LoginForm loginForm = new LoginForm("user1@gmail.com", "wrongPassword");

		MvcResult loginResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(loginForm))).andReturn();

		int status = loginResult.getResponse().getStatus();
		assertEquals(401, status);
	}

	@Test
	public void LogInWithIncorrectEmailShouldReturnUnauthorized() throws Exception {
		String uri = "/auth";
		LoginForm loginForm = new LoginForm("wrongEmail@gmail.com", "pass1");

		MvcResult loginResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(loginForm))).andReturn();

		int status = loginResult.getResponse().getStatus();
		assertEquals(401, status);
	}
}
