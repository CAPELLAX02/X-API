package com.x.backend.models.user.auth;

import com.x.backend.models.user.ApplicationUser;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "password_history",
        indexes = {
                @Index(name = "idx_user_id", columnList = "user_id", unique = true),
        }
)
public class PasswordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "changed_at", nullable = false)
    private Instant changedAt;

    public PasswordHistory() {}

    public PasswordHistory(ApplicationUser user, String password, Instant changedAt) {
        this.user = user;
        this.password = password;
        this.changedAt = changedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }

}
