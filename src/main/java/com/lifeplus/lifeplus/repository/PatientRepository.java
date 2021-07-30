package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Integer>
{
    List<Patient> findAllByUser_ActiveIsTrue();
    Optional<Patient> findByUser_IdAndUser_ActiveIsTrue(int id);

    @Query("select p from Patient p join p.user u where" +
            "(lower(u.name) like lower(concat(:name, '%')) or " +
            "lower(u.lastName) like lower(concat(:name, '%'))) and u.active = true order by u.lastName")
    Page<Patient> findAllPagedAndSortedAndFiltered(Pageable pageable, @Param("name") String name);

    @Query("select p from Patient p join p.user u where FUNCTION('day', :now - p.weightLastUpdated) >= p.weightUpdateFrequency and u.active = true")
    List<Patient> findAllForWeightNotification(@Param("now") LocalDateTime now);

}
