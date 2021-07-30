package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer>
{
    List<Plan> findAll();
    Optional<Plan> findByPatient_Id(int id);
}
