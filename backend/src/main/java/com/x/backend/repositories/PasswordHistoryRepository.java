package com.x.backend.repositories;

import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.models.entities.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    List<PasswordHistory> findTop3ByUserOrderByChangedAtDesc(ApplicationUser user);

}
