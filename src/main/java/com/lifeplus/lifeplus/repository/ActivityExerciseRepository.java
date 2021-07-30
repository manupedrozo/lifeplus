package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.ActivityExercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityExerciseRepository extends CrudRepository<ActivityExercise, Integer>
{
}
