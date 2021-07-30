package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.ActivityRoutine;
import com.lifeplus.lifeplus.model.DashboardMobile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRoutineRepository extends CrudRepository<ActivityRoutine, Integer>
{
    List<ActivityRoutine> findAllByPatient_Id(int patient);
    List<ActivityRoutine> findAllByPatient_IdAndDateBetweenOrderByDate(int patient, LocalDateTime from, LocalDateTime to);
}
