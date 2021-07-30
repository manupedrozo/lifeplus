package com.lifeplus.lifeplus.service;

import com.amazonaws.services.inspector.model.NoSuchEntityException;
import com.google.api.gax.rpc.NotFoundException;
import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.model.form.*;
import com.lifeplus.lifeplus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.management.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Manuel Pedrozo
 */
@Service
public class ReportService {

    private ActivityExerciseRepository activityExerciseRepository;
    private ActivityRoutineRepository activityRoutineRepository;
    private SensorDataRepository sensorDataRepository;
    private WeightHistoryRepository weightHistoryRepository;
    private PatientService patientService;

    @Autowired
    public ReportService(ActivityExerciseRepository activityExerciseRepository, ActivityRoutineRepository activityRoutineRepository, SensorDataRepository sensorDataRepository, PatientService patientService, WeightHistoryRepository weightHistoryRepository) {
        this.activityExerciseRepository = activityExerciseRepository;
        this.activityRoutineRepository = activityRoutineRepository;
        this.sensorDataRepository = sensorDataRepository;
        this.weightHistoryRepository = weightHistoryRepository;
        this.patientService = patientService;
    }

    public Report getReport(ReportForm reportForm) {
        Patient patient = patientService.findById(reportForm.getPatient()).orElseThrow(() -> new NoSuchElementException(String.format("Patient: %d does not exist.", reportForm.getPatient())));
        List<ActivityRoutine> activityRoutines = activityRoutineRepository.findAllByPatient_IdAndDateBetweenOrderByDate(reportForm.getPatient(), reportForm.getFrom(), reportForm.getTo());

        Report report = new Report();
        report.setFrom(reportForm.getFrom());
        report.setTo(reportForm.getTo());
        report.setPatientId(patient.getId());
        report.setReportRoutines(activityRoutines.stream().map(this::activityRoutineToReportRoutine).collect(Collectors.toList()));

        return report;
    }

    public ReportRoutine getReportRoutineByActivityRoutineId(int id) {
        ActivityRoutine activityRoutine = activityRoutineRepository.findById(id).orElseThrow(() -> new NoSuchElementException(String.format("ActivityRoutine: %d does not exist.", id)));
        return activityRoutineToReportRoutine(activityRoutine);
    }

    private ReportRoutine activityRoutineToReportRoutine(ActivityRoutine activityRoutine) {
        ReportRoutine reportRoutine = new ReportRoutine(activityRoutine);
        List<ReportExercise> reportExercises = new ArrayList<>();
        for (ActivityExercise activityExercise: activityRoutine.getExercises()) {
            ReportExercise reportExercise = new ReportExercise();
            reportExercise.setActivityExerciseId(activityExercise.getId());
            reportExercise.setData(sensorDataRepository.findAllByActivityExercise_Id(activityExercise.getId()));
            reportExercises.add(reportExercise);
        }
        reportRoutine.setReportExercises(reportExercises);
        return reportRoutine;
    }

    public List<ReportRoutine> getAllReports() {
        Optional<Patient> optionalPatient = patientService.getCurrent();
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            List<ActivityRoutine> activityRoutines = activityRoutineRepository.findAllByPatient_Id(patient.getId());
            return activityRoutines
                    .stream()
                    .map(this::activityRoutineToReportRoutine)
                    .collect(Collectors.toList());
        }

        throw new NoSuchEntityException("No user found");
    }

    public DashboardMobile getMobileDashboard() {
        Patient patient = patientService.getCurrent().orElseThrow(() -> new NoSuchElementException("Patient was not found."));
        List<ReportRoutine> allReports = getAllReports();

        int maxTime = 0;
        int sumBpm = 0;
        int totalBpm = 0;

        for (ReportRoutine activityRoutine: allReports) {
            for (ReportExercise reportExercise: activityRoutine.getReportExercises()) {
                if (reportExercise.getData() != null) {
                    totalBpm += reportExercise.getData().size();

                    for (SensorData data: reportExercise.getData()) {
                        sumBpm += data.getBpm();
                    }
                }
            }

            for (ActivityExercise exercise: activityRoutine.getActivityRoutine().getExercises()) {
                if (exercise.getDuration() != null) {
                    maxTime += exercise.getDuration();
                }
            }
        }

        double avgBpm = totalBpm != 0 ? Math.floorDiv(sumBpm, totalBpm) : 0;

        double points = 0;
        double targetPoints = 0;
        WeightEntry firstWeight = weightHistoryRepository.getFirstByPatient(patient);

        if (firstWeight != null) {
            double targetWeight = patient.getTargetWeight();
            double currentWeight = patient.getWeight();
            double basePoints = 1000 / Math.abs(firstWeight.getWeight() - targetWeight);

            targetPoints = f(Math.abs(firstWeight.getWeight() - targetWeight)) * basePoints;
            points = f(Math.abs(firstWeight.getWeight() - currentWeight)) * basePoints;
        }

        return new DashboardMobile(
                (int) points,
                (int) targetPoints,
                maxTime,
                (int) avgBpm);
    }

    Double f(Double x) {
        return Math.pow(1.05D, x)*x;
    }
}
