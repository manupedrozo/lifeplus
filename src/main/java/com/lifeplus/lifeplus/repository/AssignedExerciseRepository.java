package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.AssignedExercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignedExerciseRepository extends CrudRepository<AssignedExercise, Integer>
{
    List<AssignedExercise> findAll();
    //List<AssignedExercise> findAllByPatient_Id(int id);
}
