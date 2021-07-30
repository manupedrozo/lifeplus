package com.lifeplus.lifeplus.controller;

import com.lifeplus.lifeplus.model.Plan;
import com.lifeplus.lifeplus.model.PlanTemplate;
import com.lifeplus.lifeplus.model.Routine;
import com.lifeplus.lifeplus.model.form.ApplyTemplateForm;
import com.lifeplus.lifeplus.model.form.PlanForm;
import com.lifeplus.lifeplus.model.form.PlanTemplateForm;
import com.lifeplus.lifeplus.model.form.RoutineForm;
import com.lifeplus.lifeplus.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Manuel Pedrozo
 */
@RestController
@RequestMapping("/plan")
public class PlanController {

    private PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping()
    @Secured("ROLE_ADMIN")
    public List<Plan> getAllPlans() {
        return planService.findAll();
    }

    @GetMapping(value = "/template")
    @Secured("ROLE_ADMIN")
    public List<PlanTemplate> getAllTemplates() {
        return planService.findAllTemplates();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable("id") int id) {
        final Optional<Plan> optional = planService.findById(id);
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/template/{id}")
    public ResponseEntity<PlanTemplate> getTemplateById(@PathVariable("id") int id) {
        final Optional<PlanTemplate> optional = planService.findTemplateById(id);
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/patient/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity<Plan> getPlanByPatientId(@PathVariable("id") int id) {
        final Optional<Plan> optional = planService.findByPatientId(id);
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/template/me")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public List<PlanTemplate> getAgentTemplates() {
        return planService.findAgentTemplates();
    }

    @GetMapping(value = "/me")
    public ResponseEntity<Plan> getCurrentPatientPlan() {
        final Optional<Plan> optional = planService.findPatientPlan();
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity createPlan(@Valid @RequestBody PlanForm planForm, UriComponentsBuilder b) {
        int id = planService.save(planForm);
        UriComponents components = b.path("/plan/{id}").buildAndExpand(id);
        return ResponseEntity.created(components.toUri()).build();
    }

    @PostMapping(value = "/template")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity createTemplate(@Valid @RequestBody PlanTemplateForm planTemplateForm, UriComponentsBuilder b) {
        int id = planService.saveTemplate(planTemplateForm);
        UriComponents components = b.path("/plan/template/{id}").buildAndExpand(id);
        return ResponseEntity.created(components.toUri()).build();
    }

    @PutMapping(value = "/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity modifyPlanById(@PathVariable("id") int id, @Valid @RequestBody PlanForm planForm) {
        planService.update(id, planForm);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/template/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity modifyTemplateById(@PathVariable("id") int id, @Valid @RequestBody PlanTemplateForm templateForm) {
        planService.updateTemplate(id, templateForm);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/routine/{routineId}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity<Routine> getRoutineById(@PathVariable("id") int id, @PathVariable("routineId") int routineId) {
        final Optional<Routine> optional = planService.findRoutine(id, routineId);
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/template/{id}/routine/{routineId}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity<Routine> getTemplateRoutineById(@PathVariable("id") int id, @PathVariable("routineId") int routineId) {
        final Optional<Routine> optional = planService.findTemplateRoutine(id, routineId);
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{id}/routine")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity addRoutineByPlanId(@PathVariable("id") int id, @Valid @RequestBody RoutineForm routineForm, UriComponentsBuilder b) {
        int routineId = planService.addRoutine(id, routineForm);
        return ResponseEntity.ok(routineId);
    }

    @PostMapping(value = "/template/{id}/routine")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity addTemplateRoutineByTemplateId(@PathVariable("id") int id, @Valid @RequestBody RoutineForm routineForm, UriComponentsBuilder b) {
        int routineId = planService.addTemplateRoutine(id, routineForm);
        return ResponseEntity.ok(routineId);
    }

    @PutMapping(value = "/{id}/routine/{routineId}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity modifyRoutine(@PathVariable("id") int id, @PathVariable("routineId") int routineId, @Valid @RequestBody RoutineForm routineForm) {
        planService.updateRoutine(id, routineId, routineForm);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/template/{id}/routine/{routineId}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity modifyTemplateRoutine(@PathVariable("id") int id, @PathVariable("routineId") int routineId, @Valid @RequestBody RoutineForm routineForm) {
        planService.updateTemplateRoutine(id, routineId, routineForm);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}/routine/{routineId}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity deleteRoutine(@PathVariable("id") int id, @PathVariable("routineId") int routineId) {
        planService.removeRoutine(id, routineId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/template/{id}/routine/{routineId}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity deleteTemplateRoutine(@PathVariable("id") int id, @PathVariable("routineId") int routineId) {
        planService.removeTemplateRoutine(id, routineId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity deletePlan(@PathVariable int id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/template/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity deleteTemplate(@PathVariable int id) {
        planService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/template/apply")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity applyTemplate(@Valid @RequestBody ApplyTemplateForm applyTemplateForm, UriComponentsBuilder b) {
        int id = planService.applyTemplate(applyTemplateForm);
        UriComponents components = b.path("/plan/{id}").buildAndExpand(id);
        return ResponseEntity.created(components.toUri()).build();
    }

    @PostMapping(value = "/template/from_plan")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity createTemplateFromPlan(@Valid @RequestBody PlanTemplateForm planTemplateForm, UriComponentsBuilder b) {
        int id = planService.saveTemplateFromPlan(planTemplateForm);
        UriComponents components = b.path("/plan/template/{id}").buildAndExpand(id);
        return ResponseEntity.created(components.toUri()).build();
    }
}
