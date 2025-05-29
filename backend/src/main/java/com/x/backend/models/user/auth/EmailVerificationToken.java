package com.x.backend.models.user.auth;

import com.x.backend.models.user.ApplicationUser;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "email_verification_tokens",
        uniqueConstraints = {
        w        @UniqueConstraint(
                        name = "uq_email_verification_user",
                        columnNames = { "user_id" }
                )
        },
        indexes = {
                @Index(name = "idx_email_verification_user", columnList = "user_id"),
                @Index(name = "idx_email_verification_expiry", columnList = "expiry")
        }
)
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hashed_code", nullable = false, length = 225)
    private String hashedCode;

    @Column(name = "expiry", nullable = false)
    private Instant expiry;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true,
            referencedColumnName = "user_id"
    )
    private ApplicationUser user;

    public EmailVerificationToken() {}

    public EmailVerificationToken(String hashedCode,
                                  Instant expiry,
                                  ApplicationUser user
    ) {
        this.hashedCode = hashedCode;
        this.expiry = expiry;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashedCode() {
        return hashedCode;
    }

    public void setHashedCode(String hashedCode) {
        this.hashedCode = hashedCode;
    }

    public Instant getExpiry() {
        return expiry;
    }

    public void setExpiry(Instant expiry) {
        this.expiry = expiry;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

}
