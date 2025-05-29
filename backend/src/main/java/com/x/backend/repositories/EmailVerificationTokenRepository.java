package com.x.backend.repositories;

import com.x.backend.models.user.auth.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    void deleteByUser_Username(String username);
    Optional<EmailVerificationToken> findByUser_Username(String username);
}
