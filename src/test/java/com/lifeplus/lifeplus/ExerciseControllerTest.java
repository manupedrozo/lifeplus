package com.lifeplus.lifeplus;

import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.repository.*;
import com.lifeplus.lifeplus.service.S3Service;
import com.lifeplus.lifeplus.service.TeamService;
import com.lifeplus.lifeplus.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ExerciseControllerTest extends AbstractTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private TeamUserRepository teamUserRepository;
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private ExerciseRepository exerciseRepository;
	@Autowired
	private AssignedExerciseRepository assignedExerciseRepository;
	@Autowired
	private S3Service s3Service;

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


		Patient pat1 = new Patient(patient1, LocalDate.of(1962, 4, 23), 181, 75, 70, 7, LocalDateTime.now(), team1);
		Patient pat2 = new Patient(patient2, LocalDate.of(1977, 3, 12), 200, 80, 101, 14, LocalDateTime.now(), team2);
		Patient pat3 = new Patient(patient3, LocalDate.of(1965, 11, 11), 175, 80, 83, 10, LocalDateTime.now(), team3);

		patientRepository.save(pat1);
		patientRepository.save(pat2);
		patientRepository.save(pat3);

		Exercise exercise1 = new Exercise("Correr", "Velocidad media-alta", s3Service.getUrl("test1.bmp"));
		Exercise exercise2 = new Exercise("Biceps", "", s3Service.getUrl("test1.bmp"));
		Exercise exercise3 = new Exercise("Abdominales", "", s3Service.getUrl("test1.bmp"));
		Exercise exercise4 = new Exercise("Ciclismo", "", s3Service.getUrl("test1.bmp"));
		Exercise exercise5 = new Exercise("Estiramiento de miembros superiores", "", s3Service.getUrl("test1.bmp"));

		exerciseRepository.save(exercise1);
		exerciseRepository.save(exercise2);
		exerciseRepository.save(exercise3);
		exerciseRepository.save(exercise4);
		exerciseRepository.save(exercise5);
	}

	@After
	public void rollBackDb() {
		assignedExerciseRepository.deleteAll();
		exerciseRepository.deleteAll();
		patientRepository.deleteAll();
		teamUserRepository.deleteAll();
	    teamRepository.deleteAll();
	    userRepository.deleteAll();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getAllExercisesShouldReturnAllExercisesList() throws Exception {
		String uri = "/exercise";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Exercise[] exerciseArray = super.mapFromJson(content, Exercise[].class);

		List<Exercise> exerciseList = Arrays.stream(exerciseArray).collect(Collectors.toList());
		List<Exercise> allExercises = exerciseRepository.findAll();

		assertTrue(compareIdentifiableLists(exerciseList, allExercises));
	}

}
