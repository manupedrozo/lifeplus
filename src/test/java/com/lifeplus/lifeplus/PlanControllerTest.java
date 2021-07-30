package com.lifeplus.lifeplus;

import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.model.form.AssignedExerciseForm;
import com.lifeplus.lifeplus.model.form.PlanForm;
import com.lifeplus.lifeplus.model.form.RoutineDivisionForm;
import com.lifeplus.lifeplus.model.form.RoutineForm;
import com.lifeplus.lifeplus.repository.*;
import com.lifeplus.lifeplus.service.S3Service;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlanControllerTest extends AbstractTest {

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
	private S3Service s3Service;
	@Autowired
	private AssignedExerciseRepository assignedExerciseRepository;
	@Autowired
	private RoutineRepository routineRepository;
	@Autowired
	private RoutineDivisionRepository routineDivisionRepository;
	@Autowired
	private PlanRepository planRepository;

	@Before
	public void setUpDb() {
		User admin1   = new User("Admin", "Admin", "admin", "+54155554266", "admin@gmail.com", BCrypt.hashpw("pass", BCrypt.gensalt()), true, UserType.ADMIN);
		User patient1 = new User("Juan", "Mirra", "juanmirra", "+54955535221", "juanMirra@gmail.com", BCrypt.hashpw("juamir", BCrypt.gensalt()), true, UserType.USER);
		User patient2 = new User("Andrés", "Dominguez", "andresdominguez", "+54155556840", "andresdominguez@gmail.com", BCrypt.hashpw("anddom", BCrypt.gensalt()), true, UserType.USER);
		User patient3 = new User("Luis", "Cano", "luiscano",  "+54155550946" , "luiscano@gmail.com", BCrypt.hashpw("luican", BCrypt.gensalt()), true, UserType.USER);
		User patient4 = new User("Franco", "Hasashi", "francohasashi", "+54955583459" , "francohasashi@gmail.com", BCrypt.hashpw("frahas", BCrypt.gensalt()), true, UserType.USER);
		User medic1   = new User("Jose", "Sierra", "josesierra",  "+54955513630", "josesierra@gmail.com", BCrypt.hashpw("jossie", BCrypt.gensalt()), true, UserType.MEDIC);
		User medic2   = new User("Miguel", "Sanchez", "miguelsanchez",  "+54955004859" , "miguelsanchez@gmail.com", BCrypt.hashpw("migsan", BCrypt.gensalt()), true, UserType.MEDIC);
		User kin1     = new User("Antonio", "Riera", "antonioriera",  "+54955581421", "antonioriera@gmail.com", BCrypt.hashpw("antrie", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);
		User kin2     = new User("John", "Ferrer", "johnferrer","+54155552467", "johnferrer@gmail.com", BCrypt.hashpw("johfer", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);

		userRepository.save(admin1);
		userRepository.save(patient1);
		userRepository.save(patient2);
		userRepository.save(patient3);
		userRepository.save(patient4);
		userRepository.save(medic1);
		userRepository.save(medic2);
		userRepository.save(kin1);
		userRepository.save(kin2);

		Team team1 = new Team(patient1);
		Team team2 = new Team(patient2);
		Team team3 = new Team(patient3);
		Team team4 = new Team(patient4);
		teamRepository.save(team1);
		teamRepository.save(team2);
		teamRepository.save(team3);
		teamRepository.save(team4);

		TeamUser teamUser1 = new TeamUser(medic1, TeamRole.LEADER, team1);
		TeamUser teamUser2 = new TeamUser(kin1, TeamRole.MODIFY, team1);
		TeamUser teamUser3 = new TeamUser(medic1, TeamRole.LEADER, team2);
		TeamUser teamUser4 = new TeamUser(medic2, TeamRole.LEADER, team3);
		TeamUser teamUser5 = new TeamUser(kin2, TeamRole.MODIFY, team3);
		TeamUser teamUser6 = new TeamUser(medic1, TeamRole.LEADER, team4);
		teamUserRepository.save(teamUser1);
		teamUserRepository.save(teamUser2);
		teamUserRepository.save(teamUser3);
		teamUserRepository.save(teamUser4);
		teamUserRepository.save(teamUser5);
		teamUserRepository.save(teamUser6);

		Patient pat1 = new Patient(patient1, LocalDate.of(1962, 4, 23), 181, 75, 70, 7, LocalDateTime.now(), team1);
		Patient pat2 = new Patient(patient2, LocalDate.of(1977, 3, 12), 200, 80, 101, 14, LocalDateTime.now(), team2);
		Patient pat3 = new Patient(patient3, LocalDate.of(1965, 11, 11), 175, 80, 83, 10, LocalDateTime.now(), team3);
		Patient pat4 = new Patient(patient4, LocalDate.of(1973, 9, 4), 193, 90, 104, 5, LocalDateTime.now(), team4);

		patientRepository.save(pat1);
		patientRepository.save(pat2);
		patientRepository.save(pat3);
		patientRepository.save(pat4);

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

		AssignedExercise assignedExercise1 = new AssignedExercise(exercise1, 0, 20, null, null);
		AssignedExercise assignedExercise2 = new AssignedExercise(exercise1, 1, 10, 20, 2);
		AssignedExercise assignedExercise3 = new AssignedExercise(exercise2, 0, null, 15, 3);
		AssignedExercise assignedExercise4 = new AssignedExercise(exercise3, 0, null, 15, 3);
		AssignedExercise assignedExercise5 = new AssignedExercise(exercise4, 0, 30, null, null);
		AssignedExercise assignedExercise6 = new AssignedExercise(exercise5, 0, 10, null, null);

		assignedExerciseRepository.save(assignedExercise1);
		assignedExerciseRepository.save(assignedExercise2);
		assignedExerciseRepository.save(assignedExercise3);
		assignedExerciseRepository.save(assignedExercise4);
		assignedExerciseRepository.save(assignedExercise5);
		assignedExerciseRepository.save(assignedExercise6);

		List<AssignedExercise> assignedExercises1 = new ArrayList<>();
		assignedExercises1.add(assignedExercise1);
		assignedExercises1.add(assignedExercise2);

		RoutineDivision routineDivision1 = new RoutineDivision("Calentamiento", 0, assignedExercises1);
		List<RoutineDivision> divisions1 = new ArrayList<>();
		divisions1.add(routineDivision1);
		routineDivisionRepository.save(routineDivision1);

		Routine routine1 = new Routine("Rutina 1", "Descripcion...", 4, RoutineFrequencyType.WEEKLY, 6, divisions1);
		routineRepository.save(routine1);
		List<Routine> routines1 = new ArrayList<>();
		routines1.add(routine1);

		Plan plan1 = new Plan(routines1, pat1);
		planRepository.save(plan1);
	}

	@After
	public void rollBackDb() {
		planRepository.deleteAll();
		routineRepository.deleteAll();
		routineDivisionRepository.deleteAll();
		assignedExerciseRepository.deleteAll();
		exerciseRepository.deleteAll();
		patientRepository.deleteAll();
		teamUserRepository.deleteAll();
	    teamRepository.deleteAll();
	    userRepository.deleteAll();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getPlanByIdShouldReturnPlan() throws Exception {
		String uri = "/plan";

		Plan plan = planRepository.findAll().get(0);

		MvcResult getPlanResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + plan.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String getPlanContent = getPlanResult.getResponse().getContentAsString();
		Plan gottenPlan = super.mapFromJson(getPlanContent, Plan.class);

		assertEquals(gottenPlan.getId(), plan.getId());
	}

	@Test
	@WithMockUser(username = "juanmirra", roles = "USER")
	public void getCurrentPlanShouldReturnCurrentPlan() throws Exception {
		String uri = "/plan/me";

		User user = userRepository.findFirstByUsernameAndActiveIsTrue("juanmirra").get();
		Patient patient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(user.getId()).get();
		Plan plan = planRepository.findByPatient_Id(patient.getId()).get();

		MvcResult getPlanResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String getPlanContent = getPlanResult.getResponse().getContentAsString();
		Plan gottenPlan = super.mapFromJson(getPlanContent, Plan.class);

		assertEquals(gottenPlan.getId(), plan.getId());
	}

	@Test
	@WithMockUser(username = "josesierra", roles = "MEDIC")
	public void createPlanShouldReturnCreated() throws Exception {
		String uri = "/plan";

		User pat = userRepository.findFirstByUsernameAndActiveIsTrue("francohasashi").get();
		Patient patient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(pat.getId()).get();
		PlanForm planForm = new PlanForm();
		planForm.setPatient(patient.getId());

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(planForm))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
	}

	@Test
	@WithMockUser(username = "johnferrer", roles = "MEDIC")
	public void createPlanUnauthorizedShouldReturnUnauthorized() throws Exception {
		String uri = "/plan";

		User pat = userRepository.findFirstByUsernameAndActiveIsTrue("francohasashi").get();
		Patient patient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(pat.getId()).get();
		PlanForm planForm = new PlanForm();
		planForm.setPatient(patient.getId());

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(planForm))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(401, status);
	}

	@Test
	@WithMockUser(username = "josesierra", roles = "MEDIC")
	public void deletePlanShouldBeDeleted() throws Exception {
		String uri = "/plan";

		Plan plan = planRepository.findAll().get(0);

		MvcResult deleteResult = mvc.perform(
				MockMvcRequestBuilders.delete(uri + "/" + plan.getId()))
				.andReturn();
		int deleteStatus = deleteResult.getResponse().getStatus();
		assertEquals(204, deleteStatus);

		MvcResult getPlanResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + plan.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int getStatus = getPlanResult.getResponse().getStatus();
		assertEquals(404, getStatus);
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void createRoutineShouldReturnCreated() throws Exception {
		String uri = "/plan";

		Plan plan = planRepository.findAll().get(0);
		List<Exercise> exercises = exerciseRepository.findAll();

		RoutineForm routineForm = new RoutineForm();
		routineForm.setName("Aerobic routine");
		routineForm.setDescription("Test routine description");
		routineForm.setFrequency(3);
		routineForm.setFrequencyType(RoutineFrequencyType.WEEKLY);
		routineForm.setBorg(7);

		List<RoutineDivisionForm> routineDivisions = new ArrayList<>();
		RoutineDivisionForm routineDivisionForm1 = new RoutineDivisionForm();
		routineDivisionForm1.setName("Warm up");
		AssignedExerciseForm assignedExerciseForm1 = new AssignedExerciseForm();
		assignedExerciseForm1.setDuration(15);
		assignedExerciseForm1.setExercise(exercises.get(0).getId());
		AssignedExerciseForm assignedExerciseForm2 = new AssignedExerciseForm();
		assignedExerciseForm2.setRepetitions(10);
		assignedExerciseForm2.setSets(3);
		assignedExerciseForm2.setExercise(exercises.get(1).getId());
		List <AssignedExerciseForm> div1ae = new ArrayList<>();
		div1ae.add(assignedExerciseForm1);
		div1ae.add(assignedExerciseForm2);
		routineDivisionForm1.setAssignedExercises(div1ae);
		routineDivisions.add(routineDivisionForm1);

		RoutineDivisionForm routineDivisionForm2 = new RoutineDivisionForm();
		routineDivisionForm2.setName("Aerobic");
		AssignedExerciseForm assignedExerciseForm3 = new AssignedExerciseForm();
		assignedExerciseForm3.setDuration(15);
		assignedExerciseForm3.setExercise(exercises.get(3).getId());
		List <AssignedExerciseForm> div2ae = new ArrayList<>();
		div2ae.add(assignedExerciseForm3);
		routineDivisionForm2.setAssignedExercises(div2ae);
		routineDivisions.add(routineDivisionForm2);

		routineForm.setDivisions(routineDivisions);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + '/' + plan.getId() + "/routine")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(routineForm))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void deleteRoutineShouldBeDeleted() throws Exception {
		String uri = "/plan";

		Plan plan = planRepository.findAll().get(0);
		Routine routine = plan.getRoutines().get(0);

		MvcResult deleteResult = mvc.perform(
				MockMvcRequestBuilders.delete(uri + "/" + plan.getId() + "/routine/" + routine.getId()))
				.andReturn();
		int deleteStatus = deleteResult.getResponse().getStatus();
		assertEquals(204, deleteStatus);

		MvcResult getPlanResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + plan.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String getPlanContent = getPlanResult.getResponse().getContentAsString();
		Plan gottenPlan = super.mapFromJson(getPlanContent, Plan.class);

		assertTrue(gottenPlan.getRoutines().stream().noneMatch(r -> r.getId() == routine.getId()));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void updateRoutineShouldBeUpdated() throws Exception {
		String uri = "/plan";

		List<Exercise> exercises = exerciseRepository.findAll();
		Plan plan = planRepository.findAll().get(0);
		Routine routine = plan.getRoutines().get(0);

		RoutineForm routineForm = new RoutineForm();
		routineForm.setName("Aerobic routine");
		routineForm.setDescription("Test routine description");
		routineForm.setFrequency(3);
		routineForm.setFrequencyType(RoutineFrequencyType.WEEKLY);
		routineForm.setBorg(7);

		List<RoutineDivisionForm> routineDivisions = new ArrayList<>();
		RoutineDivisionForm routineDivisionForm1 = new RoutineDivisionForm();
		routineDivisionForm1.setName("Warm up");
		AssignedExerciseForm assignedExerciseForm1 = new AssignedExerciseForm();
		assignedExerciseForm1.setDuration(15);
		assignedExerciseForm1.setExercise(exercises.get(0).getId());
		AssignedExerciseForm assignedExerciseForm2 = new AssignedExerciseForm();
		assignedExerciseForm2.setRepetitions(10);
		assignedExerciseForm2.setSets(3);
		assignedExerciseForm2.setExercise(exercises.get(1).getId());
		List <AssignedExerciseForm> div1ae = new ArrayList<>();
		div1ae.add(assignedExerciseForm1);
		div1ae.add(assignedExerciseForm2);
		routineDivisionForm1.setAssignedExercises(div1ae);
		routineDivisions.add(routineDivisionForm1);

		routineForm.setDivisions(routineDivisions);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri + '/' + plan.getId() + "/routine/" + routine.getId())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(routineForm))).andReturn();

		MvcResult getPlanResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + plan.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(204, status);

		String getPlanContent = getPlanResult.getResponse().getContentAsString();
		Plan gottenPlan = super.mapFromJson(getPlanContent, Plan.class);
		Routine updatedRoutine = gottenPlan.getRoutines().get(0);

		assertEquals(1, gottenPlan.getRoutines().size());
		assertEquals(routineForm.getName(), updatedRoutine.getName());
		assertEquals(routineForm.getDescription(), updatedRoutine.getDescription());
		assertEquals(routineForm.getDivisions().size(), updatedRoutine.getDivisions().size());
	}
}
