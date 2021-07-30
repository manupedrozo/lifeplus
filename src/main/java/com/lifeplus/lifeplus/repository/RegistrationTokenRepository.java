package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, Integer> {
}
