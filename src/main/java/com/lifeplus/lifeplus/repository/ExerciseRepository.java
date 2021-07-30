package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.Exercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Integer>
{
    List<Exercise> findAll();
    List<Exercise> findAllByCategory_Id(int category);
}
