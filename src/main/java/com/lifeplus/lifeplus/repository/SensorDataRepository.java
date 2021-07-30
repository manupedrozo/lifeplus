package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.SensorData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDataRepository extends CrudRepository<SensorData, Integer>
{
    public List<SensorData> findAllByActivityExercise_Id(int id);
}
