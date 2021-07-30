package com.lifeplus.lifeplus;

import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.model.form.*;
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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReportControllerTest extends AbstractTest {

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
	@Autowired
	private ActivityExerciseRepository activityExerciseRepository;
	@Autowired
	private ActivityRoutineRepository activityRoutineRepository;
	@Autowired
	private SensorDataRepository sensorDataRepository;

	private ActivityRoutine activityRoutine;

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
		TeamUser teamUser6 = new TeamUser(medic2, TeamRole.LEADER, team4);
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

		ActivityExercise activityExercise1 = new ActivityExercise(assignedExercise1, 15, null, null);
		activityExerciseRepository.save(activityExercise1);

		ActivityRoutine activityRoutine1 = new ActivityRoutine(routine1, pat1, 7, "", "", LocalDateTime.of(2019, 8, 19, 18, 25, 10), Collections.singletonList(activityExercise1));
		activityRoutineRepository.save(activityRoutine1);
		this.activityRoutine = activityRoutine1;

		SensorData sensorData1 = new SensorData(activityExercise1, 90, LocalDateTime.of(2019, 8, 19, 18, 25, 15));
		SensorData sensorData2 = new SensorData(activityExercise1, 92, LocalDateTime.of(2019, 8, 19, 18, 25, 30));
		SensorData sensorData3 = new SensorData(activityExercise1, 89, LocalDateTime.of(2019, 8, 19, 18, 25, 40));
		sensorDataRepository.save(sensorData1);
		sensorDataRepository.save(sensorData2);
		sensorDataRepository.save(sensorData3);
	}

	@After
	public void rollBackDb() {
	    sensorDataRepository.deleteAll();
	    activityRoutineRepository.deleteAll();
	    activityExerciseRepository.deleteAll();
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
	public void getReportShouldReturnReport() throws Exception {
		String uri = "/report";

		User user = userRepository.findFirstByUsernameAndActiveIsTrue("juanmirra").get();
		Patient patient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(user.getId()).get();
		LocalDateTime from = LocalDateTime.of(2019, 8, 18, 0, 0, 0);
		LocalDateTime to = LocalDateTime.of(2019, 8, 20, 0, 0, 0);

		ReportForm reportForm = new ReportForm();
		reportForm.setFrom(from);
		reportForm.setTo(to);
		reportForm.setPatient(patient.getId());

		MvcResult reportResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(reportForm))).andReturn();
		String reportContent = reportResult.getResponse().getContentAsString();
		Report gottenReport = super.mapFromJson(reportContent, Report.class);

		List<ActivityExercise> activityExercises = new ArrayList<>();
		activityRoutineRepository.findAllByPatient_IdAndDateBetweenOrderByDate(patient.getId(), from, to).forEach(ar -> activityExercises.addAll(ar.getExercises()));
		List<Identifiable> gottenActivityExercises = new ArrayList<>();
		gottenReport.getReportRoutines().forEach(r -> r.getReportExercises().forEach(re -> gottenActivityExercises.add(new Identifiable() {
			@Override
			public int getId() {
				return re.getActivityExerciseId();
			}
		})));
		assertTrue(compareIdentifiableLists(activityExercises, gottenActivityExercises));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getOutOfDateReportShouldReturnEmptyReport() throws Exception {
		String uri = "/report";

		User user = userRepository.findFirstByUsernameAndActiveIsTrue("juanmirra").get();
		Patient patient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(user.getId()).get();
		LocalDateTime from = LocalDateTime.of(2019, 8, 15, 0, 0, 0);
		LocalDateTime to = LocalDateTime.of(2019, 8, 17, 0, 0, 0);

		ReportForm reportForm = new ReportForm();
		reportForm.setFrom(from);
		reportForm.setTo(to);
		reportForm.setPatient(patient.getId());

		MvcResult reportResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(reportForm))).andReturn();
		String reportContent = reportResult.getResponse().getContentAsString();
		Report gottenReport = super.mapFromJson(reportContent, Report.class);

		assertTrue(gottenReport.getReportRoutines().isEmpty());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getReportRoutineByActivityIdShouldReturnReportRoutine() throws Exception {
		String uri = "/report/routine/";

		MvcResult reportResult = mvc.perform(MockMvcRequestBuilders.get(uri + activityRoutine.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String reportContent = reportResult.getResponse().getContentAsString();
		ReportRoutine gottenReportRoutine = super.mapFromJson(reportContent, ReportRoutine.class);

		assertEquals(activityRoutine.getId(), gottenReportRoutine.getActivityRoutine().getId());
	}
}
