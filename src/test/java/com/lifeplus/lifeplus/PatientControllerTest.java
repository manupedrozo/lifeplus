package com.lifeplus.lifeplus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.model.Dto.WeightUpdateFrequency;
import com.lifeplus.lifeplus.model.form.PatientForm;
import com.lifeplus.lifeplus.model.form.WeightForm;
import com.lifeplus.lifeplus.model.form.WeightReportForm;
import com.lifeplus.lifeplus.repository.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PatientControllerTest extends AbstractTest {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PatientRepository patientRepository;
	@Autowired
	TeamRepository teamRepository;
	@Autowired
	TeamUserRepository teamUserRepository;
	@Autowired
	WeightHistoryRepository weightHistoryRepository;

	@Before
	public void setUpDb() {
		// users
		User admin1   = new User("Admin", "Admin", "admin", "+54155554266", "admin@gmail.com", BCrypt.hashpw("pass", BCrypt.gensalt()), true, UserType.ADMIN);
		User patient1 = new User("Juan", "Mirra", "juanmirra", "+54955535221", "juanMirra@gmail.com", BCrypt.hashpw("juamir", BCrypt.gensalt()), true, UserType.USER);
		User patient2 = new User("Andrés", "Dominguez", "andresdominguez", "+54155556840", "andresdominguez@gmail.com", BCrypt.hashpw("anddom", BCrypt.gensalt()), true, UserType.USER);
		User patient3 = new User("Luis", "Cano", "luiscano",  "+54155550946" , "luiscano@gmail.com", BCrypt.hashpw("luican", BCrypt.gensalt()), true, UserType.USER);
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

		WeightEntry weightEntry1a = new WeightEntry(pat1, LocalDateTime.now().minusDays(2), 72);
		WeightEntry weightEntry1b = new WeightEntry(pat1, LocalDateTime.now(), pat1.getWeight());
		WeightEntry weightEntry2 = new WeightEntry(pat2, LocalDateTime.now(), pat2.getWeight());
		WeightEntry weightEntry3a = new WeightEntry(pat3, LocalDateTime.now().minusDays(8), 81);
		WeightEntry weightEntry3b = new WeightEntry(pat3, LocalDateTime.now().minusDays(4), 82);
		WeightEntry weightEntry3c = new WeightEntry(pat3, LocalDateTime.now(), pat3.getWeight());

		weightHistoryRepository.save(weightEntry1a);
		weightHistoryRepository.save(weightEntry1b);
		weightHistoryRepository.save(weightEntry2);
		weightHistoryRepository.save(weightEntry3a);
		weightHistoryRepository.save(weightEntry3b);
		weightHistoryRepository.save(weightEntry3c);
	}

	@After
	public void rollBackDb() {
		weightHistoryRepository.deleteAll();
		patientRepository.deleteAll();
		teamUserRepository.deleteAll();
		teamRepository.deleteAll();
	    userRepository.deleteAll();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getAllPatientsShouldReturnAllPatientsList() throws Exception {
		String uri = "/patient";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Patient[] patientArray = super.mapFromJson(content, Patient[].class);

		List<Patient> patientList = Arrays.stream(patientArray).collect(Collectors.toList());
		List<Patient> allPatients = patientRepository.findAllByUser_ActiveIsTrue();

		assertTrue(compareIdentifiableLists(allPatients, patientList));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getAllPatientsPagedAndFilteredShouldReturnCorrectPage() throws Exception {
	    getAllPagedAndFilteredTest(1, 1, "j");
		getAllPagedAndFilteredTest(0, 10, "j");
		getAllPagedAndFilteredTest(2, 2, "");
	}

	private void getAllPagedAndFilteredTest(int pageNumber, int size, String name) throws Exception {
		String uri = "/patient/paged?page=" + pageNumber + (size > 0 ? "&size=" + size : "") + "&name=" + name;
		if (size == 0) size = 10;

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();

		Page<Patient> page = super.mapFromJson(content, new TypeReference<CustomPageImpl<Patient>>(){});

		Page<Patient> patientPage = patientRepository.findAllPagedAndSortedAndFiltered(PageRequest.of(pageNumber, size), name);

		assertEquals(patientPage.getPageable().getPageNumber(), page.getPageable().getPageNumber());
		assertEquals(patientPage.getPageable().getPageSize(), page.getPageable().getPageSize());
		assertTrue(compareIdentifiableLists(patientPage.getContent(), page.getContent()));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getPatientByIdShouldReturnPatient() throws Exception {
		String uri = "/patient";

		Patient patient = patientRepository.findAllByUser_ActiveIsTrue().get(0);

		MvcResult getPatientResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + patient.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String getPatientContent = getPatientResult.getResponse().getContentAsString();
		Patient gottenPatient = super.mapFromJson(getPatientContent, Patient.class);

		assertEquals(gottenPatient.getId(), patient.getId());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getPatientByUserIdShouldReturnPatient() throws Exception {
		String uri = "/patient/by_user";

		Patient patient = patientRepository.findAllByUser_ActiveIsTrue().get(0);

		MvcResult getPatientResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + patient.getUser().getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String getPatientContent = getPatientResult.getResponse().getContentAsString();
		Patient gottenPatient = super.mapFromJson(getPatientContent, Patient.class);

		assertEquals(gottenPatient.getId(), patient.getId());
	}

	@Test
	@WithMockUser(username = "luiscano", roles = "USER")
	public void getCurrentPatientShouldReturnCurrentPatient() throws Exception {
		String uri = "/patient/me";

		User user = userRepository.findFirstByUsernameAndActiveIsTrue("luiscano").get();
		Patient patient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(user.getId()).get();

		MvcResult getPatientResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String getPatientContent = getPatientResult.getResponse().getContentAsString();
		Patient gottenPatient = super.mapFromJson(getPatientContent, Patient.class);

		assertEquals(gottenPatient.getId(), patient.getId());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void editPatientShouldBeEdited() throws Exception {
		String uri = "/patient";

		Patient patient = patientRepository.findAllByUser_ActiveIsTrue().get(0);

		PatientForm patientForm = new PatientForm(LocalDate.now(), 220, 95, WeightUpdateFrequency.DAYS15);

		MvcResult editResult = mvc.perform(MockMvcRequestBuilders.put(uri + "/" + patient.getId())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(patientForm))).andReturn();
		int editStatus = editResult.getResponse().getStatus();
		assertEquals(204, editStatus);

		MvcResult getPatientResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + patient.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String getPatientContent = getPatientResult.getResponse().getContentAsString();
		Patient editedPatient = super.mapFromJson(getPatientContent, Patient.class);

		assertEquals(patientForm.getBirthDate(), editedPatient.getBirthDate());
		assertEquals(patientForm.getHeight(), editedPatient.getHeight());
		assertEquals(patientForm.getTargetWeight(), editedPatient.getTargetWeight(), 0);
		assertEquals(patientForm.getWeightUpdateFrequency(), editedPatient.getEnumWeightUpdateFrequency());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWeightHistoryShouldReturnHistory() throws Exception {
		String uri = "/patient";

		Patient patient = patientRepository.findAllByUser_ActiveIsTrue().get(0);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + patient.getId() + "/weight")
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		WeightEntry[] patientArray = super.mapFromJson(content, WeightEntry[].class);

		List<WeightEntry> gottenList = Arrays.stream(patientArray).collect(Collectors.toList());
		List<WeightEntry> list = weightHistoryRepository.getAllByPatient_IdOrderByDateDesc(patient.getId());

		assertTrue(compareIdentifiableLists(list, gottenList));
	}

	@Test
	@WithMockUser(username = "juanmirra", roles = "USER")
	public void getCurrentWeightHistoryShouldReturnHistory() throws Exception {
		String uri = "/patient/weight/history/me";

		User user = userRepository.findFirstByUsernameAndActiveIsTrue("juanmirra").get();
		Patient patient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(user.getId()).get();

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		WeightEntry[] weightArray = super.mapFromJson(content, WeightEntry[].class);

		List<WeightEntry> gottenList = Arrays.stream(weightArray).collect(Collectors.toList());
		List<WeightEntry> list = weightHistoryRepository.getAllByPatient_IdOrderByDateDesc(patient.getId());

		assertTrue(compareIdentifiableLists(list, gottenList));
	}

	@Test
	@WithMockUser(username = "juanmirra", roles = "USER")
	public void updateCurrentWeightShouldUpdate() throws Exception {
		String uri = "/patient/weight/me";

		User user = userRepository.findFirstByUsernameAndActiveIsTrue("juanmirra").get();
		Patient patient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(user.getId()).get();

		List<WeightEntry> oldList = weightHistoryRepository.getAllByPatient_IdOrderByDateDesc(patient.getId());

		WeightForm weightForm = new WeightForm(83);
		MvcResult editResult = mvc.perform(MockMvcRequestBuilders.put(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(weightForm))).andReturn();

		int editStatus = editResult.getResponse().getStatus();
		assertEquals(204, editStatus);

		List<WeightEntry> newList = weightHistoryRepository.getAllByPatient_IdOrderByDateDesc(patient.getId());
		WeightEntry lastEntry = newList.get(0);
		Patient updatedPatient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(user.getId()).get();

		assertEquals(oldList.size() + 1, newList.size());
		assertEquals(weightForm.getWeight(), lastEntry.getWeight(), 0);
		assertEquals(weightForm.getWeight(), updatedPatient.getWeight(), 0);
		assertEquals(lastEntry.getDate(), updatedPatient.getWeightLastUpdated());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWeightShouldUpdate() throws Exception {
		String uri = "/patient";

		User user = userRepository.findFirstByUsernameAndActiveIsTrue("juanmirra").get();
		Patient patient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(user.getId()).get();

		List<WeightEntry> oldList = weightHistoryRepository.getAllByPatient_IdOrderByDateDesc(patient.getId());

		WeightForm weightForm = new WeightForm(83);
		MvcResult editResult = mvc.perform(MockMvcRequestBuilders.put(uri + "/" + patient.getId() + "/weight")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(weightForm))).andReturn();

		int editStatus = editResult.getResponse().getStatus();
		assertEquals(204, editStatus);

		List<WeightEntry> newList = weightHistoryRepository.getAllByPatient_IdOrderByDateDesc(patient.getId());
		WeightEntry lastEntry = newList.get(0);
		Patient updatedPatient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(user.getId()).get();

		assertEquals(oldList.size() + 1, newList.size());
		assertEquals(weightForm.getWeight(), lastEntry.getWeight(), 0);
		assertEquals(weightForm.getWeight(), updatedPatient.getWeight(), 0);
		assertEquals(lastEntry.getDate(), updatedPatient.getWeightLastUpdated());
	}

	@Test
	@WithMockUser(username = "luiscano", roles = "USER")
	public void getCurrentTargetHRShouldBeCorrect() throws Exception {
		String uri = "/patient/targethr/me";

		MvcResult getFreqResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String getHRContent = getFreqResult.getResponse().getContentAsString();
		TargetHeartRate gottenHR = super.mapFromJson(getHRContent, TargetHeartRate.class);

		TargetHeartRate expectedHR = new TargetHeartRate(83, 140, 165);

		assertEquals(expectedHR, gottenHR);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getTargetHRByIdShouldBeCorrect() throws Exception {
		String uri = "/patient/";

		User user = userRepository.findFirstByUsernameAndActiveIsTrue("luiscano").get();
		Patient patient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(user.getId()).get();

		MvcResult getFreqResult = mvc.perform(MockMvcRequestBuilders.get(uri + patient.getId() + "/targethr")
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String getHRContent = getFreqResult.getResponse().getContentAsString();
		TargetHeartRate gottenHR = super.mapFromJson(getHRContent, TargetHeartRate.class);

		TargetHeartRate expectedHR = new TargetHeartRate(83, 140, 165);

		assertEquals(expectedHR, gottenHR);
  }

  @Test
	@WithMockUser(roles = "ADMIN")
	public void getWeightReportShouldReturnWeightReport() throws Exception {

		User user = userRepository.findFirstByUsernameAndActiveIsTrue("juanmirra").get();
		Patient patient = patientRepository.findByUser_IdAndUser_ActiveIsTrue(user.getId()).get();
		int patientId = patient.getId();
		LocalDateTime from = LocalDateTime.now().minusDays(7);
		LocalDateTime to = LocalDateTime.now();

		WeightReportForm form = new WeightReportForm();
		form.setFrom(from);
		form.setTo(to);
		form.setPatient(patientId);

		String uri = "/patient/weight";

		MvcResult reportResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(form))).andReturn();
		String reportContent = reportResult.getResponse().getContentAsString();
		WeightReport gottenReport = super.mapFromJson(reportContent, WeightReport.class);

		List<WeightEntry> gottenEntries = gottenReport.getReportEntries();
		List<WeightEntry> weightEntries = new ArrayList<>(weightHistoryRepository.findAllByPatient_IdAndDateBetweenOrderByDate(patientId, from, to));
		System.out.println("gotten entries:");
		System.out.println(gottenEntries);
		System.out.println("---");
		System.out.println("real entries:");
		System.out.println(weightEntries);

		assertTrue(compareIdentifiableLists(gottenEntries, weightEntries));
	}
}
