package com.lifeplus.lifeplus.controller.ws.stomp;

import com.lifeplus.lifeplus.model.form.ActivityRoutineForm;
import com.lifeplus.lifeplus.model.form.ActivityQuestionnaireForm;
import com.lifeplus.lifeplus.model.form.SensorDataForm;
import com.lifeplus.lifeplus.model.ws.ActivityIds;
import com.lifeplus.lifeplus.service.ActivityRoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

/**
 * @author Manuel Pedrozo
 */
@Controller()
public class ExerciseWSController {

    private ActivityRoutineService activityRoutineService;

    @Autowired
    public ExerciseWSController(ActivityRoutineService activityRoutineService) {
        this.activityRoutineService = activityRoutineService;
    }

    @MessageMapping("/activity_routine")
    @SendToUser("/queue/reply")
    public ActivityIds createActivityRoutine(@Valid ActivityRoutineForm activityRoutineForm) {
        ActivityIds activityIds = activityRoutineService.save(activityRoutineForm);
        return activityIds;
    }

    @MessageMapping("/data")
    public void sensorData(@Valid SensorDataForm sensorDataForm) {
        activityRoutineService.save(sensorDataForm);
    }

    @MessageMapping("/questionnaire")
    public void questionnaire(@Valid ActivityQuestionnaireForm activityQuestionnaireForm) {
        activityRoutineService.questionnaireUpdate(activityQuestionnaireForm);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/error")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
