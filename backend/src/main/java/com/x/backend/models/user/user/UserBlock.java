package com.x.backend.models.user.user;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_blocks",
        indexes = {
                @Index(name = "idx_blocked_user", columnList = "blocked_user_id"),
                @Index(name = "idx_blocking_user", columnList = "blocking_user_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_block",
                        columnNames = {
                                "blocking_user_id",
                                "blocked_user_id"
                        }
                )
        }
)
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocking_user_id", nullable = false)
    private ApplicationUser blockingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_user_id", nullable = false)
    private ApplicationUser blockedUser;

    @Column(name = "blocked_at", nullable = false)
    private LocalDateTime blockedAt;

    public UserBlock() {}

    public UserBlock(
            Long id,
            ApplicationUser blockingUser,
            ApplicationUser blockedUser,
            LocalDateTime blockedAt
    ) {
        this.id = id;
        this.blockingUser = blockingUser;
        this.blockedUser = blockedUser;
        this.blockedAt = blockedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser getBlockingUser() {
        return blockingUser;
    }

    public void setBlockingUser(ApplicationUser blockingUser) {
        this.blockingUser = blockingUser;
    }

    public ApplicationUser getBlockedUser() {
        return blockedUser;
    }

    public void setBlockedUser(ApplicationUser blockedUser) {
        this.blockedUser = blockedUser;
    }

    public LocalDateTime getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(LocalDateTime blockedAt) {
        this.blockedAt = blockedAt;
    }

}
