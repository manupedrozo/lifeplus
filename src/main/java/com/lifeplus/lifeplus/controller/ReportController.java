package com.lifeplus.lifeplus.controller;

import com.amazonaws.services.inspector.model.NoSuchEntityException;
import com.lifeplus.lifeplus.model.DashboardMobile;
import com.lifeplus.lifeplus.model.Report;
import com.lifeplus.lifeplus.model.ReportRoutine;
import com.lifeplus.lifeplus.model.form.ReportForm;
import com.lifeplus.lifeplus.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Manuel Pedrozo
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping()
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity<Report> report(@Valid @RequestBody ReportForm reportForm) {
        Report report = reportService.getReport(reportForm);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/all/me")
    @Secured({"ROLE_USER"})
    public ResponseEntity getAll() {
        try {
            return ResponseEntity.ok(reportService.getAllReports());
        } catch (NoSuchEntityException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/routine/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity<ReportRoutine> getReportRoutineByActivityRoutineId(@PathVariable("id") int id) {
        return ResponseEntity.ok(reportService.getReportRoutineByActivityRoutineId(id));
    }

    @GetMapping("/dashboard")
    @Secured({"ROLE_USER"})
    public ResponseEntity<DashboardMobile> getMobileDashboard() {
        return ResponseEntity.ok(reportService.getMobileDashboard());
    }
}
