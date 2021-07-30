package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.Routine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends CrudRepository<Routine, Integer>
{

}
