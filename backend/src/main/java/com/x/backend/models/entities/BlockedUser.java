package com.x.backend.models.entities;

import com.x.backend.models.AbstractBaseEntity;
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
public class BlockedUser extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocking_user_id", nullable = false)
    private ApplicationUser blockingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_user_id", nullable = false)
    private ApplicationUser blockedUser;

    @Column(name = "blocked_at", nullable = false)
    private LocalDateTime blockedAt;

    public BlockedUser() {}

    public BlockedUser(
            ApplicationUser blockingUser,
            ApplicationUser blockedUser,
            LocalDateTime blockedAt
    ) {
        this.blockingUser = blockingUser;
        this.blockedUser = blockedUser;
        this.blockedAt = blockedAt;
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
