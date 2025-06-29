package com.x.backend.repositories;

import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.auth.ValidAccessToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface ValidAccessTokenRepository extends JpaRepository<ValidAccessToken, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ValidAccessToken t WHERE t.user = :user")
    void deleteAllByUser(@Param("user") ApplicationUser user);

    @Modifying
    @Transactional
    @Query("DELETE FROM ValidAccessToken t WHERE t.expiresAt < :now")
    void deleteAllByExpiresAtBefore(@Param("now") Instant now);

    boolean existsByJti(String jti);

}
