package com.lifeplus.lifeplus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.model.form.PasswordForm;
import com.lifeplus.lifeplus.model.form.UserEditForm;
import com.lifeplus.lifeplus.repository.PatientRepository;
import com.lifeplus.lifeplus.repository.TeamRepository;
import com.lifeplus.lifeplus.repository.TeamUserRepository;
import com.lifeplus.lifeplus.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class UserControllerTest extends AbstractTest {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PatientRepository patientRepository;
	@Autowired
	TeamUserRepository teamUserRepository;
	@Autowired
	TeamRepository teamRepository;

	@Before
	public void setUpDb() {
		User user1  = new User("User1", "Last1", "username1", "1111111", "user1@gmail.com", BCrypt.hashpw("pass1", BCrypt.gensalt()), true, UserType.USER);
		User user2	= new User("User2", "Last2", "username2", "2222222","user2@gmail.com", BCrypt.hashpw("pass2", BCrypt.gensalt()), true, UserType.USER);
		User admin 	= new User("Admin", "Admin", "admin", "1234567", "admin@gmail.com", BCrypt.hashpw("pass", BCrypt.gensalt()), true, UserType.ADMIN);
		User medic1 = new User("Jose", "Sierra", "josesierra",  "+54955513630", "josesierra@gmail.com", BCrypt.hashpw("jossie", BCrypt.gensalt()), true, UserType.MEDIC);
		User medic2 = new User("Miguel", "Sanchez", "miguelsanchez",  "+54955004859" , "miguelsanchez@gmail.com", BCrypt.hashpw("migsan", BCrypt.gensalt()), true, UserType.MEDIC);
		User kin1   = new User("Antonio", "Riera", "antonioriera",  "+54955581421", "antonioriera@gmail.com", BCrypt.hashpw("antrie", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);
		User kin2   = new User("John", "Ferrer", "johnferrer","+54155552467", "johnferrer@gmail.com", BCrypt.hashpw("johfer", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);
		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(admin);
		userRepository.save(medic1);
		userRepository.save(medic2);
		userRepository.save(kin1);
		userRepository.save(kin2);
	}

	@After
	public void rollBackDb() {
		patientRepository.deleteAll();
		teamUserRepository.deleteAll();
		teamRepository.deleteAll();
	    userRepository.deleteAll();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getAllUsersShouldReturnAllUsersList() throws Exception {
		String uri = "/user";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		User[] userArray = super.mapFromJson(content, User[].class);

		List<User> userList = Arrays.stream(userArray).collect(Collectors.toList());
		List<User> allUsers = userRepository.findAllByActiveIsTrue();

		assertTrue(compareIdentifiableLists(allUsers, userList));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getAllUsersPagedAndFilteredShouldReturnCorrectPage() throws Exception {
		UserType[] type1 = {UserType.USER};
		getAllPagedAndFilteredTest(0, 10, type1, "us");
		UserType[] type2 = {UserType.USER, UserType.ADMIN};
		getAllPagedAndFilteredTest(2, 3, type2, "");
		UserType[] type3 = UserType.values();
		getAllPagedAndFilteredTest(1, 3, type3, "");
		UserType[] type4 = {UserType.ADMIN, UserType.MEDIC};
		getAllPagedAndFilteredTest(0, 4, type4, "s");
	}

	private void getAllPagedAndFilteredTest(int pageNumber, int size, UserType[] type, String name) throws Exception {
		String uri = "/user/paged?page=" + pageNumber + (size > 0 ? "&size=" + size : "") + "&type=" + UserType.commaSeparated(type) + "&name=" + name;
		if (size == 0) size = 10;

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();

		Page<User> page = super.mapFromJson(content, new TypeReference<CustomPageImpl<User>>(){});

		Page<User> userPage = userRepository.findPagedAndFilteredAndSorted(PageRequest.of(pageNumber, size, Sort.by("lastName")), type, name);

		assertEquals(userPage.getPageable().getPageNumber(), page.getPageable().getPageNumber());
		assertEquals(userPage.getPageable().getPageSize(), page.getPageable().getPageSize());
		assertTrue(compareIdentifiableLists(userPage.getContent(), page.getContent()));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getUserByIdShouldReturnUser() throws Exception {
		String uri = "/user";

		User user = userRepository.findAllByActiveIsTrue().get(0);

		MvcResult getUserResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + user.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String getUserContent = getUserResult.getResponse().getContentAsString();
		User gottenUser = super.mapFromJson(getUserContent, User.class);

		assertEquals(gottenUser.getId(), user.getId());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getUserByUsernameShouldReturnUser() throws Exception {
		String uri = "/user/by_username";

		MvcResult getUserResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/username1")
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String getUserContent = getUserResult.getResponse().getContentAsString();
		User gottenUser = super.mapFromJson(getUserContent, User.class);

		assertEquals(gottenUser.getUsername(), "username1");
	}

	@Test
	@WithMockUser(username = "username1", roles = "USER")
	public void getCurrentUserShouldReturnCurrentUser() throws Exception {
		String uri = "/user/me";

		MvcResult getUserResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String getUserContent = getUserResult.getResponse().getContentAsString();
		User gottenUser = super.mapFromJson(getUserContent, User.class);

		assertEquals(gottenUser.getEmail(), "user1@gmail.com");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void addUserShouldReturnCreated() throws Exception {
		String uri = "/user";
		User user = new User("TestUser", "lastname", "test", "12312312", "user@gmail.com", "pass", true, UserType.USER);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(fullMapToJson(user))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void addAlreadyExistingUserShouldReturnConflict() throws Exception {
		String uri = "/user";
		User user = new User("User1", "Last1", "username1", "1111111", "user1@gmail.com", "pass1", true, UserType.USER);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(fullMapToJson(user))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(409, status);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void deleteUserShouldBeDeleted() throws Exception {
		String uri = "/user";

		MvcResult getAllUserResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String getAllUserContent = getAllUserResult.getResponse().getContentAsString();
		User[] userList = super.mapFromJson(getAllUserContent, User[].class);

		User user = userList[0];

		MvcResult deleteResult = mvc.perform(
				MockMvcRequestBuilders.delete(uri + "/" + user.getId()))
				.andReturn();
		int deleteStatus = deleteResult.getResponse().getStatus();
		assertEquals(204, deleteStatus);

		MvcResult getUserResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + user.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int getStatus = getUserResult.getResponse().getStatus();
		assertEquals(404, getStatus);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void editUserShouldBeEdited() throws Exception {
		String uri = "/user";

		MvcResult getAllUserResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String getAllUserContent = getAllUserResult.getResponse().getContentAsString();
		User[] userList = super.mapFromJson(getAllUserContent, User[].class);

		User user = userList[0];
		UserEditForm userEditForm = new UserEditForm("editedName", "editedLastName", "edited", "3214567" ,"edited@gmail.com");

		MvcResult editResult = mvc.perform(MockMvcRequestBuilders.put(uri + "/" + user.getId())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(userEditForm))).andReturn();
		int editStatus = editResult.getResponse().getStatus();
		assertEquals(204, editStatus);

		MvcResult getUserResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + user.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String getUserContent = getUserResult.getResponse().getContentAsString();
		User editedUser = super.mapFromJson(getUserContent, User.class);

		assertEquals(userEditForm.getName(), editedUser.getName());
		assertEquals(userEditForm.getLastName(), editedUser.getLastName());
		assertEquals(userEditForm.getUsername(), editedUser.getUsername());
		assertEquals(userEditForm.getPhone(), editedUser.getPhone());
		assertEquals(userEditForm.getEmail(), editedUser.getEmail());
	}

	@Test
	@WithMockUser(username = "username1", roles = "USER")
	public void editCurrentUserShouldBeEdited() throws Exception {
		String uri = "/user/me";

		UserEditForm userEditForm = new UserEditForm("editedName", "editedLastName", "edited", "3214567" ,"edited@gmail.com");

		MvcResult editResult = mvc.perform(MockMvcRequestBuilders.put(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(userEditForm))).andReturn();
		int editStatus = editResult.getResponse().getStatus();
		assertEquals(200, editStatus);

		setAuthentication("edited", "pass1", "USER");

		MvcResult getUserResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String getUserContent = getUserResult.getResponse().getContentAsString();
		User editedUser = super.mapFromJson(getUserContent, User.class);

		assertEquals(userEditForm.getName(), editedUser.getName());
		assertEquals(userEditForm.getLastName(), editedUser.getLastName());
		assertEquals(userEditForm.getEmail(), editedUser.getEmail());
	}

	@Test
	@WithMockUser(username = "username1", roles = "USER")
	public void verifyCorrectPasswordShouldReturnNoContent() throws Exception {
		String uri = "/user/me/verify_password";

		PasswordForm passwordForm = new PasswordForm("pass1");

		MvcResult verifyPasswordResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(passwordForm))).andReturn();

		int verifyStatus = verifyPasswordResult.getResponse().getStatus();
		assertEquals(204, verifyStatus);
	}

	@Test
	@WithMockUser(username = "username1", roles = "USER")
	public void verifyIncorrectPasswordShouldReturnNotFound() throws Exception {
		String uri = "/user/me/verify_password";

		PasswordForm passwordForm = new PasswordForm("wrongPassword");

		MvcResult verifyPasswordResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(passwordForm))).andReturn();

		int verifyStatus = verifyPasswordResult.getResponse().getStatus();
		assertEquals(404, verifyStatus);
	}

	@Test
	@WithMockUser(username = "username1", roles = "USER")
	public void editCurrentPasswordShouldBeEdited() throws Exception {
		String uri = "/user/me/change_password";
		String uriVerify = "/user/me/verify_password";

		PasswordForm passwordForm = new PasswordForm("editedpass");

		MvcResult editResult = mvc.perform(MockMvcRequestBuilders.put(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(passwordForm))).andReturn();
		int editStatus = editResult.getResponse().getStatus();
		assertEquals(204, editStatus);

		MvcResult verifyPasswordResult = mvc.perform(MockMvcRequestBuilders.post(uriVerify)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(passwordForm))).andReturn();

		int verifyStatus = verifyPasswordResult.getResponse().getStatus();
		assertEquals(204, verifyStatus);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void editPasswordByIdShouldBeEdited() throws Exception {
		String uri = "/user/";

		User user = userRepository.findFirstByUsernameAndActiveIsTrue("username1").get();

		PasswordForm passwordForm = new PasswordForm("editedpass");

		MvcResult editResult = mvc.perform(MockMvcRequestBuilders.put(uri + user.getId() + "/change_password")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(passwordForm))).andReturn();
		int editStatus = editResult.getResponse().getStatus();
		assertEquals(204, editStatus);

		assertTrue(
				userRepository.findById(user.getId())
						.filter(u -> BCrypt.checkpw(passwordForm.getPassword(), u.getPassword()))
						.isPresent()
		);
	}
}
