package com.lifeplus.lifeplus.controller;

import com.lifeplus.lifeplus.model.Patient;
import com.lifeplus.lifeplus.model.TargetHeartRate;
import com.lifeplus.lifeplus.model.WeightEntry;
import com.lifeplus.lifeplus.model.WeightReport;
import com.lifeplus.lifeplus.model.form.PatientForm;
import com.lifeplus.lifeplus.model.form.WeightForm;
import com.lifeplus.lifeplus.model.form.WeightReportForm;
import com.lifeplus.lifeplus.service.PatientService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping()
    @Secured("ROLE_ADMIN")
    public List<Patient> getAllPatients() {
        return patientService.findAll();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/paged")
    public ResponseEntity<Page<Patient>> getAllPatientsPagedAndFiltered(
            @ApiParam(value = "Query param for 'page number'") @Valid @RequestParam(value = "page") int page,
            @ApiParam(value = "Query param for 'page size'") @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @ApiParam(value = "Query param for 'name' filter") @Valid @RequestParam(value = "name", required = false, defaultValue = "") String name
    ) {
        if (size == 0) size = 10;
        Page<Patient> patientPage = patientService.findAllPagedAndFiltered(page, size, name);
        return ResponseEntity.ok(patientPage);
    }

    @GetMapping(value = "/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") int id) {
        final Optional<Patient> optional = patientService.findById(id);
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/by_user/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity<Patient> getPatientByUserId(@PathVariable("id") int id) {
        final Optional<Patient> optional = patientService.findByUserId(id);
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity modifyPatientById(@PathVariable("id") int id, @Valid @RequestBody PatientForm patientForm) {
        patientService.update(id, patientForm);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/me")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Patient> getCurrentPatient() {
        final Optional<Patient> optional = patientService.getCurrent();
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/me")
    @Secured({"ROLE_USER"})
    public ResponseEntity modifyCurrentPatient(@Valid @RequestBody PatientForm patientForm) {
        return patientService.updateCurrent(patientForm) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/weight/history/me")
    @Secured({"ROLE_USER"})
    public List<WeightEntry> getCurrentWeightHistory() {
        return patientService.getCurrentWeightHistory();
    }

    @GetMapping(value = "/weight/me")
    @Secured({"ROLE_USER"})
    public WeightEntry getCurrentWeight() {
        return patientService.getCurrentWeight();
    }

    @GetMapping(value = "{id}/weight")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public List<WeightEntry> getWeightHistoryById(@PathVariable("id") int id) {
        return patientService.getWeightHistoryById(id);
    }

    @PostMapping(value = "/weight")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity getWeightReportById(@Valid @RequestBody WeightReportForm form) {
        WeightReport weightReport = patientService.getWeightReport(form);
        return ResponseEntity.ok(weightReport);
    }

    @PutMapping(value = "/{id}/weight")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity updateWeightById(@PathVariable("id") int id, @Valid @RequestBody WeightForm weightForm) {
        patientService.updateWeightById(id, weightForm);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/weight/me")
    public ResponseEntity updateCurrentWeight(@Valid @RequestBody WeightForm weightForm) {
        patientService.updateCurrentWeight(weightForm);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/targethr/me")
    @Secured({"ROLE_USER"})
    public ResponseEntity<TargetHeartRate> getCurrentTargetHR() {
        final TargetHeartRate targetHR = patientService.getCurrentTargetHR();
        return ResponseEntity.ok(targetHR);
    }

    @GetMapping(value = "/{id}/targethr")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity<TargetHeartRate> getTargetHRById(@PathVariable("id") int id) {
        final TargetHeartRate targetHR = patientService.getTargetHR(id);
        return ResponseEntity.ok(targetHR);
    }
}
