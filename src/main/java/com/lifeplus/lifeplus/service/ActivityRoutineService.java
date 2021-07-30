package com.lifeplus.lifeplus.service;

import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.model.Dto.ActivityRoutineBasicDto;
import com.lifeplus.lifeplus.model.form.ActivityRoutineForm;
import com.lifeplus.lifeplus.model.form.ActivityQuestionnaireForm;
import com.lifeplus.lifeplus.model.form.OfflineActivityRoutineForm;
import com.lifeplus.lifeplus.model.form.SensorDataForm;
import com.lifeplus.lifeplus.model.ws.ActivityIds;
import com.lifeplus.lifeplus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author Manuel Pedrozo
 */
@Service
public class ActivityRoutineService {

    private AssignedExerciseRepository assignedExerciseRepository;
    private ActivityExerciseRepository activityExerciseRepository;
    private ActivityRoutineRepository activityRoutineRepository;
    private RoutineRepository routineRepository;
    private SensorDataRepository sensorDataRepository;
    private PatientService patientService;

    @Autowired
    public ActivityRoutineService(AssignedExerciseRepository assignedExerciseRepository, ActivityExerciseRepository activityExerciseRepository, ActivityRoutineRepository activityRoutineRepository, RoutineRepository routineRepository, SensorDataRepository sensorDataRepository, PatientService patientService) {
        this.assignedExerciseRepository = assignedExerciseRepository;
        this.activityExerciseRepository = activityExerciseRepository;
        this.activityRoutineRepository = activityRoutineRepository;
        this.routineRepository = routineRepository;
        this.sensorDataRepository = sensorDataRepository;
        this.patientService = patientService;
    }

    public ActivityRoutine findById(int id) {
        return activityRoutineRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(String.format("ActivityRoutine: %d does not exist.", id)));
    }

    private ActivityExercise findActivityExerciseById(int id) {
        return activityExerciseRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(String.format("ActivityExercise: %d does not exist.", id)));
    }

    public ActivityIds save(ActivityRoutineForm activityRoutineForm) {
        Routine routine = routineRepository.findById(activityRoutineForm.getRoutine()).orElseThrow(() -> new NoSuchElementException(String.format("Routine: %d does not exist.", activityRoutineForm.getRoutine())));
        Patient patient = patientService.findById(activityRoutineForm.getPatient()).orElseThrow(
                () -> new NoSuchElementException(String.format("Patient: %d does not exist.", activityRoutineForm.getPatient())));

        ActivityRoutine activityRoutine = new ActivityRoutine(
                routine,
                patient,
                activityRoutineForm.getDate()
        );

        List<ActivityExercise> activityExercises = new ArrayList<>();
        activityRoutineForm.getActivityExercises().forEach(aef -> {
            AssignedExercise assignedExercise = assignedExerciseRepository.findById(aef.getAssignedExercise()).orElseThrow(
                    () -> new NoSuchElementException(String.format("AssignedExercise: %d does not exist.", aef.getAssignedExercise())));
            ActivityExercise activityExercise = new ActivityExercise(assignedExercise, aef.getDuration(), aef.getRepetitions(), aef.getSets());
            activityExercises.add(activityExercise);
        });
        List<Integer> activityExercisesIds = new ArrayList<>();
        activityExercises.forEach(ae -> {
            activityExerciseRepository.save(ae);
            activityExercisesIds.add(ae.getId());
        });
        activityRoutine.setExercises(activityExercises);

        activityRoutineRepository.save(activityRoutine);

        return new ActivityIds(activityRoutine.getId(), activityExercisesIds);
    }

    public void questionnaireUpdate(ActivityQuestionnaireForm activityQuestionnaireForm) {
        ActivityRoutine activityRoutine = findById(activityQuestionnaireForm.getActivityRoutine());
        activityRoutine.setPatientBorg(activityQuestionnaireForm.getBorg());
        activityRoutine.setIncidents(activityQuestionnaireForm.getIncidents());
        activityRoutine.setNotes(activityQuestionnaireForm.getNotes());
        activityRoutineRepository.save(activityRoutine);
    }

    public int save(SensorDataForm sensorDataForm) {
        ActivityExercise activityExercise = findActivityExerciseById(sensorDataForm.getActivityExercise());
        SensorData sensorData = new SensorData(activityExercise, sensorDataForm.getBpm(), sensorDataForm.getDate());
        sensorDataRepository.save(sensorData);
        return sensorData.getId();
    }

    public int save(OfflineActivityRoutineForm offlineActivityRoutineForm) {
        Routine routine = routineRepository.findById(offlineActivityRoutineForm.getRoutine()).orElseThrow(() -> new NoSuchElementException(String.format("Routine: %d does not exist.", offlineActivityRoutineForm.getRoutine())));
        Patient patient = patientService.getCurrent().orElseThrow(() -> new NoSuchElementException(String.format("Patient does not exist.")));

        ActivityRoutine activityRoutine = new ActivityRoutine(
                        routine,
                        patient,
                        offlineActivityRoutineForm.getPatientBorg(),
                        offlineActivityRoutineForm.getNotes(),
                        offlineActivityRoutineForm.getIncidents(),
                        offlineActivityRoutineForm.getDate());

        List<ActivityExercise> activityExercises = new ArrayList<>();
        List<SensorData> sensorDataList = new ArrayList<>();
        offlineActivityRoutineForm.getActivityExercises().forEach(aef -> {
            AssignedExercise assignedExercise = assignedExerciseRepository.findById(aef.getAssignedExercise()).orElseThrow(
                    () -> new NoSuchElementException(String.format("AssignedExercise: %d does not exist.", aef.getAssignedExercise())));
            ActivityExercise activityExercise = new ActivityExercise(assignedExercise, aef.getDuration(), aef.getRepetitions(), aef.getSets());
            activityExercises.add(activityExercise);
            aef.getSensorData().forEach(sd -> sensorDataList.add(new SensorData(activityExercise, sd.getBpm(), sd.getDate())));
        });
        activityExercises.forEach(ae -> activityExerciseRepository.save(ae));
        sensorDataList.forEach(sd -> sensorDataRepository.save(sd));

        activityRoutine.setExercises(activityExercises);
        activityRoutineRepository.save(activityRoutine);
        return activityRoutine.getId();
    }

    public List<ActivityRoutineBasicDto> getCurrentHistory() {
        Patient patient = patientService.getCurrent().orElseThrow(() -> new NoSuchElementException("Patient does not exist."));
        List<ActivityRoutine> activityRoutines = activityRoutineRepository.findAllByPatient_Id(patient.getId());
        return activityRoutines.stream()
                .map(activityRoutine -> new ActivityRoutineBasicDto(activityRoutine.getId(), activityRoutine.getDate(), activityRoutine.getName()))
                .collect(Collectors.toList());
    }

    @MessageExceptionHandler
    @SendToUser("/error")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
