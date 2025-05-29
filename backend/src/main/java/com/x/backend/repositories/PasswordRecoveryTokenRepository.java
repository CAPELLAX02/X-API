package com.x.backend.repositories;

import com.x.backend.models.user.auth.PasswordRecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Long> {
    void deleteByUser_Username(String username);
    Optional<PasswordRecoveryToken> findByUser_Username(String username);
}
