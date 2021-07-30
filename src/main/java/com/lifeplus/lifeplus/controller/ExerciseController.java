package com.lifeplus.lifeplus.controller;

import com.lifeplus.lifeplus.model.Category;
import com.lifeplus.lifeplus.model.CategorizedExercises;
import com.lifeplus.lifeplus.model.Exercise;
import com.lifeplus.lifeplus.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Manuel Pedrozo
 */
@RestController
@RequestMapping("/exercise")
public class ExerciseController {

    private ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping()
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public List<Exercise> getAllExercises() {
        return exerciseService.findAll();
    }

    @GetMapping("/category")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public List<Category> getAllCategories() {
        return exerciseService.findAllCategories();
    }

    @GetMapping("/by_category/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public List<Exercise> getAllByCategory(@PathVariable("id") int id) {
        return exerciseService.findAllByCategory(id);
    }

    @GetMapping("/categorized")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public List<CategorizedExercises> getAllCategorized() {
        return exerciseService.findAllCategorized();
    }
}
