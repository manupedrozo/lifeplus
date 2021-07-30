package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Integer> {
    List<Team> findAllByPatient_ActiveIsTrue();

    Optional<Team> findFirstByIdAndPatient_ActiveIsTrue(Integer id);

    List<Team> findFirstByPatient_IdAndPatient_ActiveIsTrue(Integer id);

    @Query("select t from Team t join t.patient p where p.id = :id and p.active = true order by p.lastName")
    List<Team> findAllByUserPatientId(@Param("id") Integer id);

    @Query("select t from Team t join t.patient p join t.teamUsers u where u.user.id = :id and u.user.active = true and p.active = true order by p.lastName")
    List<Team> findAllByUserTeamUserId(@Param("id") Integer id);

    @Query("select t from Team t join t.patient p where" +
            "(lower(p.name) like lower(concat(:name, '%')) or " +
            "lower(p.lastName) like lower(concat(:name, '%'))) and p.active = true order by p.lastName")
    Page<Team> findPagedAndFilteredAndSorted(Pageable pageable, @Param("name") String name);

    @Query("select t from Team t join t.patient p join t.teamUsers u where u.user.id = :id and" +
            "(lower(p.name) like lower(concat(:name, '%')) or " +
            "lower(p.lastName) like lower(concat(:name, '%'))) and p.active = true order by p.lastName")
    Page<Team> findPagedAndFilteredAndSorted(Pageable pageable, @Param("id") Integer id, @Param("name") String name);


}
