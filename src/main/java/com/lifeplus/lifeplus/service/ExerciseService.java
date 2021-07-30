package com.lifeplus.lifeplus.service;

import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.repository.AssignedExerciseRepository;
import com.lifeplus.lifeplus.repository.CategoryRepository;
import com.lifeplus.lifeplus.repository.ExerciseRepository;
import com.lifeplus.lifeplus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Manuel Pedrozo
 */
@Service
public class ExerciseService {

    private ExerciseRepository exerciseRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.exerciseRepository = exerciseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    public Optional<Exercise> findById(int id) {
        return exerciseRepository.findById(id);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Exercise> findAllByCategory(int id) {
        return exerciseRepository.findAllByCategory_Id(id);
    }

    public List<CategorizedExercises> findAllCategorized() {
        List<CategorizedExercises> categorizedExercises = new ArrayList<>();
        for (Category category: findAllCategories()) {
            List<Exercise> exercises = exerciseRepository.findAllByCategory_Id(category.getId());
            CategorizedExercises categorized = new CategorizedExercises(category.getId(), category.getName(), exercises);
            categorizedExercises.add(categorized);
        }
        return categorizedExercises;
    }

    /*
    public List<AssignedExercise> findAssignedExercisesByPatientId(int id) {
        return assignedExerciseRepository.findAllByPatient_Id(id);
    }

    public List<AssignedExercise> findCurrentAssignedExercises() {
        Optional<Patient> optionalPatient = patientService.getCurrent();
        if(optionalPatient.isPresent()) {
            return findAssignedExercisesByPatientId(optionalPatient.get().getId());
        }
        return new ArrayList<>();
    }
     */
}
