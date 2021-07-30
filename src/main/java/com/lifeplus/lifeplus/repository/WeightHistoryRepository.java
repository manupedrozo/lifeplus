package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.Patient;
import com.lifeplus.lifeplus.model.WeightEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeightHistoryRepository extends CrudRepository<WeightEntry, Integer>
{
    List<WeightEntry> getAllByPatient_IdOrderByDateDesc(int id);

    List<WeightEntry> getAllByPatient_IdOrderByDateAsc(int id);

    WeightEntry getFirstByPatient(Patient patient);

    List<WeightEntry> findAllByPatient_IdAndDateBetweenOrderByDate(int patient, LocalDateTime from, LocalDateTime to);
}
