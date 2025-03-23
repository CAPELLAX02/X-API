package com.x.backend.repositories;

import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.models.entities.PrivacySettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivacySettingsRepository extends JpaRepository<PrivacySettings, Long> {

    Optional<PrivacySettings> findByUser(ApplicationUser user);

}
