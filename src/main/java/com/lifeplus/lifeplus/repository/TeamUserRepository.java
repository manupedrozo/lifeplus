package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.TeamUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamUserRepository extends CrudRepository<TeamUser, Integer>
{
    Optional<TeamUser> findByIdAndUser_ActiveIsTrue(Integer id);
    List<TeamUser> findAllByUser_IdAndUser_ActiveIsTrue(int id);
    List<TeamUser> findAllByUser_Id(int id);

    @Query("select u from TeamUser u join u.team t join t.patient p where u.user.id = :userId and u.user.active = true and p.id = :patientId and p.active = true")
    Optional<TeamUser> findByUserIdAndPatientUserId(@Param("userId") Integer userId, @Param("patientId") Integer patientId);
}
