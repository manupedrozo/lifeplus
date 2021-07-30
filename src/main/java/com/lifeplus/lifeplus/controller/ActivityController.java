package com.lifeplus.lifeplus.controller;

import com.lifeplus.lifeplus.model.DashboardMobile;
import com.lifeplus.lifeplus.model.Dto.ActivityRoutineBasicDto;
import com.lifeplus.lifeplus.model.form.ActivityQuestionnaireForm;
import com.lifeplus.lifeplus.model.form.ActivityRoutineForm;
import com.lifeplus.lifeplus.model.form.OfflineActivityRoutineForm;
import com.lifeplus.lifeplus.model.ws.ActivityIds;
import com.lifeplus.lifeplus.service.ActivityRoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Manuel Pedrozo
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {

    private ActivityRoutineService activityRoutineService;

    @Autowired
    public ActivityController(ActivityRoutineService activityRoutineService) {
        this.activityRoutineService = activityRoutineService;
    }

    @PostMapping("/offline/me")
    @Secured({"ROLE_USER"})
    public ResponseEntity createOfflineActivityRoutine(@Valid @RequestBody OfflineActivityRoutineForm offlineActivityRoutineForm) {
        int id = activityRoutineService.save(offlineActivityRoutineForm);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/me")
    @Secured({"ROLE_USER"})
    public ResponseEntity<ActivityIds> createActivityRoutine(@Valid @RequestBody ActivityRoutineForm activityRoutineForm) {
        ActivityIds activityIds = activityRoutineService.save(activityRoutineForm);
        return ResponseEntity.ok(activityIds);
    }

    @PostMapping("/questionnaire/me")
    @Secured({"ROLE_USER"})
    public ResponseEntity activityQuestionnaire(@Valid @RequestBody ActivityQuestionnaireForm activityQuestionnaireForm) {
        activityRoutineService.questionnaireUpdate(activityQuestionnaireForm);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history/me")
    @Secured({"ROLE_USER"})
    public List<ActivityRoutineBasicDto> getCurrentHistory() {
        return activityRoutineService.getCurrentHistory();
    }
}
