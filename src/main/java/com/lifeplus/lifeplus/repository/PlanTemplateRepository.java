package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.PlanTemplate;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PlanTemplateRepository extends CrudRepository<PlanTemplate, Integer> {
    List<PlanTemplate> findAll();
    List<PlanTemplate> findAllByCreator_Id(int id);
}
