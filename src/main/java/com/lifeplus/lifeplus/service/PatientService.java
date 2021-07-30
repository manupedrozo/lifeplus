package com.lifeplus.lifeplus.service;

import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.model.form.PatientForm;
import com.lifeplus.lifeplus.model.form.WeightForm;
import com.lifeplus.lifeplus.model.form.WeightReportForm;
import com.lifeplus.lifeplus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PatientService {

    private PatientRepository patientRepository;
    private TeamRepository teamRepository;
    private UserRepository userRepository;
    private WeightHistoryRepository weightHistoryRepository;
    private PlanRepository planRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository, TeamRepository teamRepository, UserRepository userRepository, WeightHistoryRepository weightHistoryRepository, PlanRepository planRepository) {
        this.patientRepository = patientRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.weightHistoryRepository = weightHistoryRepository;
        this.planRepository = planRepository;
    }

    public List<Patient> findAll() {
        return patientRepository.findAllByUser_ActiveIsTrue();
    }

    public Page<Patient> findAllPagedAndFiltered(int page, int size, String name) {
        return patientRepository.findAllPagedAndSortedAndFiltered(PageRequest.of(page, size), name);
    }

    public Optional<Patient> findByUserId(int userId) {
        return patientRepository.findByUser_IdAndUser_ActiveIsTrue(userId);
    }

    public Optional<Patient> findById(int id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> createFromUser(User user) {
        if (user.getType() == UserType.USER) {
            Team team = new Team(user);
            teamRepository.save(team);
            Patient patient = new Patient();
            patient.setUser(user);
            patient.setTeam(team);
            patientRepository.save(patient);
            Plan plan = new Plan(patient);
            planRepository.save(plan);
            return Optional.of(patient);
        }
        return Optional.empty();
    }

    public boolean update(int id, PatientForm patientForm) {
        final Optional<Patient> patientOptional = findById(id);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            patientForm.patch(patient);
            patientRepository.save(patient);
            return true;
        }
        return false;
    }

    public boolean updateByUserId(int userId, PatientForm patientForm) {
        final Optional<Patient> patientOptional = findByUserId(userId);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            patientForm.patch(patient);
            patientRepository.save(patient);
            return true;
        }
        return false;
    }

    public Optional<Patient> getCurrent() {
        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> userOptional = userRepository.findFirstByUsernameAndActiveIsTrue(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getType() == UserType.USER) {
                return findByUserId(user.getId());
            }
        }
        return Optional.empty();
    }

    public boolean updateCurrent(PatientForm patientForm) {
        Optional<Patient> patientOptional = getCurrent();
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            patientForm.patch(patient);
            patientRepository.save(patient);
            return true;
        }
        return false;
    }

    public void updateWeightById(int patientId, WeightForm weightForm) {
        LocalDateTime date = LocalDateTime.now();
        Patient patient = findById(patientId).orElseThrow(() -> new NoSuchElementException(String.format("Patient: %d does not exist.", patientId)));
        patient.setWeight(weightForm.getWeight());
        patient.setWeightLastUpdated(date);
        patientRepository.save(patient);

        WeightEntry weightEntry = new WeightEntry(patient, date, weightForm.getWeight());
        weightHistoryRepository.save(weightEntry);
    }

    public void updateCurrentWeight(WeightForm weightForm) {
        LocalDateTime date = LocalDateTime.now();
        Patient patient = getCurrent().orElseThrow(() -> new NoSuchElementException("Patient does not exist."));
        patient.setWeight(weightForm.getWeight());
        patient.setWeightLastUpdated(date);
        patientRepository.save(patient);

        WeightEntry weightEntry = new WeightEntry(patient, date, weightForm.getWeight());
        weightHistoryRepository.save(weightEntry);
    }

    public List<WeightEntry> getCurrentWeightHistory() {
        Patient patient = getCurrent().orElseThrow(() -> new NoSuchElementException("Patient does not exist."));
        return weightHistoryRepository.getAllByPatient_IdOrderByDateAsc(patient.getId());
    }

    public List<WeightEntry> getWeightHistoryById(int patientId) {
        return weightHistoryRepository.getAllByPatient_IdOrderByDateAsc(patientId);
    }

    private TargetHeartRate getTargetHR(Patient patient) {
        int years = LocalDate.now().getYear() - patient.getBirthDate().getYear();
        return HRTable(years);
    }

    public TargetHeartRate getTargetHR(int id) {
        Patient patient = findById(id).orElseThrow(() -> new NoSuchElementException(String.format("Patient: %d does not exist.", id)));
        return getTargetHR(patient);
    }

    public TargetHeartRate getCurrentTargetHR() {
        Patient patient = getCurrent().orElseThrow(() -> new NoSuchElementException("Patient does not exist."));
        return getTargetHR(patient);
    }

    // http://www.heart.org/HEARTORG/HealthyLiving/PhysicalActivity/FitnessBasics/Target-HeartRates_UCM_434341_Article.jsp#.XZOjm7dJaDZ
    private TargetHeartRate HRTable(int age) {
        if (age < 20) return new TargetHeartRate(100, 170, 200);
        else if (age < 30) return new TargetHeartRate(95, 162, 190);
        else if (age < 35) return new TargetHeartRate(93, 157, 185);
        else if (age < 40) return new TargetHeartRate(90, 153, 180);
        else if (age < 45) return new TargetHeartRate(88, 149, 175);
        else if (age < 50) return new TargetHeartRate(85, 145, 170);
        else if (age < 55) return new TargetHeartRate(83, 140, 165);
        else if (age < 60) return new TargetHeartRate(80, 136, 160);
        else if (age < 65) return new TargetHeartRate(78, 132, 155);
        else return new TargetHeartRate(75, 128, 150);
    }

    public WeightEntry getCurrentWeight() {
        Patient patient = getCurrent().orElseThrow(() -> new NoSuchElementException("Patient does not exist."));
        WeightEntry weight = new WeightEntry();
        weight.setWeight(patient.getWeight());
        return weight;
    }

    public WeightReport getWeightReport(WeightReportForm form) {
        Patient patient = findById(form.getPatient()).orElseThrow(() -> new NoSuchElementException(String.format("Patient: %d does not exist.", form.getPatient())));
        List<WeightEntry> entries = weightHistoryRepository.findAllByPatient_IdAndDateBetweenOrderByDate(form.getPatient(), form.getFrom(), form.getTo());
        WeightReport weightReport = new WeightReport();
        weightReport.setFrom(form.getFrom());
        weightReport.setTo(form.getTo());
        weightReport.setPatient(patient);
        weightReport.setReportEntries(entries);

        return weightReport;
    }
}
