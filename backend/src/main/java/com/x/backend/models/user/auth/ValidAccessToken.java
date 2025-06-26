package com.x.backend.models.user.auth;

import com.x.backend.models.user.ApplicationUser;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "valid_access_tokens")
public class ValidAccessToken {

    @Id
    private String jti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    public ValidAccessToken() {}

    public ValidAccessToken(String jti, ApplicationUser user, Instant expiresAt) {
        this.jti = jti;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

}
