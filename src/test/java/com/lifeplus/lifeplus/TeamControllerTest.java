package com.lifeplus.lifeplus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.model.form.TeamForm;
import com.lifeplus.lifeplus.model.form.TeamUserForm;
import com.lifeplus.lifeplus.repository.TeamRepository;
import com.lifeplus.lifeplus.repository.TeamUserRepository;
import com.lifeplus.lifeplus.repository.UserRepository;
import com.lifeplus.lifeplus.service.TeamService;
import com.lifeplus.lifeplus.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TeamControllerTest extends AbstractTest {

    @Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TeamService teamService;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private TeamUserRepository teamUserRepository;

	@Before
	public void setUpDb() {
		// users
		User admin1   = new User("Admin", "Admin", "admin", "+54155554266", "admin@gmail.com", BCrypt.hashpw("pass", BCrypt.gensalt()), true, UserType.ADMIN);
		User patient1 = new User("Juan", "Mirra", "juanmirra", "+54955535221", "juanMirra@gmail.com", BCrypt.hashpw("juamir", BCrypt.gensalt()), true, UserType.USER);
		User patient2 = new User("Andrés", "Dominguez", "andresdominguez", "+54155556840", "andresdominguez@gmail.com", BCrypt.hashpw("anddom", BCrypt.gensalt()), true, UserType.USER);
		User patient3 = new User("Luis", "Cano", "luiscano",  "+54155550946" , "luiscano@gmail.com", BCrypt.hashpw("luican", BCrypt.gensalt()), true, UserType.USER);
		User patient4 = new User("Franco", "Hasashi", "francohasashi", "+54955583459" , "francohasashi@gmail.com", BCrypt.hashpw("frahas", BCrypt.gensalt()), true, UserType.USER);
		User patient5 = new User("Quin", "Alonso", "quinalonso", "+54955507833", "quinalonso@gmail.com", BCrypt.hashpw("quialo", BCrypt.gensalt()), true, UserType.USER);
		User medic1a   = new User("Jose", "Sierra", "josesierra",  "+54955513630", "josesierra@gmail.com", BCrypt.hashpw("jossie", BCrypt.gensalt()), true, UserType.MEDIC);
		User medic1b   = new User("Tomas", "Bulacio", "tomasbulacio",  "+54955513630", "tomasbulacio@gmail.com", BCrypt.hashpw("tombul", BCrypt.gensalt()), true, UserType.MEDIC);
		User medic2   = new User("Miguel", "Sanchez", "miguelsanchez",  "+54955004859" , "miguelsanchez@gmail.com", BCrypt.hashpw("migsan", BCrypt.gensalt()), true, UserType.MEDIC);
		User medic3   = new User("Romeo", "Santos", "romeosantos",  "+54955004859" , "romeosantos@gmail.com", BCrypt.hashpw("romsan", BCrypt.gensalt()), true, UserType.MEDIC);
		User kin1a     = new User("Antonio", "Riera", "antonioriera",  "+54955581421", "antonioriera@gmail.com", BCrypt.hashpw("antrie", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);
		User kin1b     = new User("John", "Ferrer", "johnferrer","+54155552467", "johnferrer@gmail.com", BCrypt.hashpw("johfer", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);
		User kin2     = new User("David", "Bisbal", "davidbisbal","+54155552467", "davidbisbal@gmail.com", BCrypt.hashpw("davbis", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);

		userRepository.save(admin1);
		userRepository.save(patient1);
		userRepository.save(patient2);
		userRepository.save(patient3);
		userRepository.save(patient4);
		userRepository.save(patient5);
		userRepository.save(medic1a);
		userRepository.save(medic1b);
		userRepository.save(medic2);
		userRepository.save(medic3);
		userRepository.save(kin1a);
		userRepository.save(kin1b);
		userRepository.save(kin2);


		// teams
		Team team1 = new Team(patient1);
		Team team2 = new Team(patient2);
		Team team3 = new Team(patient3);
		teamRepository.save(team1);
		teamRepository.save(team2);
		teamRepository.save(team3);

		// teamUsers
		TeamUser teamUserMedic1a = new TeamUser(medic1a, TeamRole.LEADER, team1);
		TeamUser teamUserMedic1b = new TeamUser(medic1b, TeamRole.LEADER, team1);
		TeamUser teamUserKin1a = new TeamUser(kin1a, TeamRole.MODIFY, team1);
		TeamUser teamUserKin1b = new TeamUser(kin1b, TeamRole.VIEW, team1);
		TeamUser teamUserMedic2 = new TeamUser(medic2, TeamRole.LEADER, team2);
		TeamUser teamUserKin2 = new TeamUser(kin2, TeamRole.MODIFY, team2);
		TeamUser teamUserMedic3 = new TeamUser(medic3, TeamRole.LEADER, team3);
		teamUserRepository.save(teamUserMedic1a);
		teamUserRepository.save(teamUserMedic1b);
		teamUserRepository.save(teamUserKin1a);
		teamUserRepository.save(teamUserKin1b);
		teamUserRepository.save(teamUserMedic2);
		teamUserRepository.save(teamUserKin2);
		teamUserRepository.save(teamUserMedic3);
	}

	@After
	public void rollBackDb() {
		teamUserRepository.deleteAll();
	    teamRepository.deleteAll();
	    userRepository.deleteAll();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getAllTeamsShouldReturnAllTeamsList() throws Exception {
		String uri = "/team";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Team[] teamArray = super.mapFromJson(content, Team[].class);

		List<Team> teamList = Arrays.stream(teamArray).collect(Collectors.toList());
		List<Team> allTeams = teamRepository.findAllByPatient_ActiveIsTrue();

		assertTrue(compareIdentifiableLists(allTeams, teamList));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getAllTeamsPagedAndFilteredShouldReturnCorrectPage() throws Exception {
	    getAllPagedAndFilteredTest(0, 2, "a");
		getAllPagedAndFilteredTest(0, 10, "j");
		getAllPagedAndFilteredTest(2, 2, "");
	}

	private void getAllPagedAndFilteredTest(int pageNumber, int size, String name) throws Exception {
		String uri = "/team/paged?page=" + pageNumber + (size > 0 ? "&size=" + size : "") + "&name=" + name;
		if (size == 0) size = 10;

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();

		Page<Team> page = super.mapFromJson(content, new TypeReference<CustomPageImpl<Team>>(){});

		Page<Team> teamPage = teamRepository.findPagedAndFilteredAndSorted(PageRequest.of(pageNumber, size), name);

		assertEquals(teamPage.getPageable().getPageNumber(), page.getPageable().getPageNumber());
		assertEquals(teamPage.getPageable().getPageSize(), page.getPageable().getPageSize());
		assertTrue(compareIdentifiableLists(teamPage.getContent(), page.getContent()));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getTeamByIdShouldReturnTeam() throws Exception {
		String uri = "/team";

		Team toGet = null;
		List<Team> list = teamService.findAll();
		if (!list.isEmpty())
			toGet = list.get(0);
		assertNotNull(toGet);

		MvcResult getResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + toGet.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = getResult.getResponse().getStatus();
		assertEquals(200, status);

		String getContent = getResult.getResponse().getContentAsString();
		Team team = super.mapFromJson(getContent, Team.class);

		assertEquals(toGet.getId(), team.getId());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getTeamByUserIdShouldReturnUsersTeam() throws Exception {
		String uri = "/team/user";

		User user = null;
		Iterator<User> iterator = userService.findAllByType(UserType.USER).iterator();
		if (iterator.hasNext())
			user = iterator.next();
		assertNotNull(user);
		assertEquals(UserType.USER, user.getType());
		System.out.println("user id is: " + user.getId());

		MvcResult getResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + user.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = getResult.getResponse().getStatus();
		assertEquals(200, status);

		String getContent = getResult.getResponse().getContentAsString();
		Team[] teams = super.mapFromJson(getContent, Team[].class);
		System.out.println("team ids:");
		for (Team team : teams) {
			System.out.println(team.getId());
		}

		assertEquals(1, teams.length);
		assertEquals(teams[0].getPatient().getId(), user.getId());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void editTeamShouldBeEdited() throws Exception {
		User medic = new User("med", "medlast", "medlas", "3123491", "medlast@gmail.com", BCrypt.hashpw("password", BCrypt.gensalt()), true, UserType.MEDIC);
		User kin = new User("kin", "kinlast", "kinlas", "1293847", "kinlast@gmail.com", BCrypt.hashpw("password", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);
		userRepository.save(medic);
		userRepository.save(kin);

		String uri = "/team";

		Team team = null;
		Iterator<Team> teams = teamService.findAll().iterator();
		if(teams.hasNext()) {
			team = teams.next();
		}
		assertNotNull(team);

		List<TeamUserForm> teamUserForms = new ArrayList<>();
		teamUserForms.add(new TeamUserForm(medic.getId(), TeamRole.LEADER, team.getId()));
		teamUserForms.add(new TeamUserForm(kin.getId(), TeamRole.VIEW, team.getId()));

		TeamForm teamForm = new TeamForm(teamUserForms);

		MvcResult editResult = mvc.perform(MockMvcRequestBuilders.put(uri + "/" + team.getId())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(teamForm))).andReturn();
		int editStatus = editResult.getResponse().getStatus();
		assertEquals(204, editStatus);

		MvcResult getTeamResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + team.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String getTeamContent = getTeamResult.getResponse().getContentAsString();
		Team editedTeam = super.mapFromJson(getTeamContent, Team.class);

		assertEquals(team.getId(), editedTeam.getId());
		assertEquals(teamForm.getTeamUserForms().size(), editedTeam.getTeamUsers().size());
	}
}
