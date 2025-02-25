package com.x.backend.models.entities;

import com.x.backend.models.AbstractBaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "message_reads",
        indexes = {
                @Index(
                        name = "idx_message_read_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_message_read_message",
                        columnList = "message_id"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_message_read",
                        columnNames = {
                                "user_id",
                                "message_id"
                        }
                )
        }
)
public class MessageRead extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "read_at", nullable = false)
    private LocalDateTime readAt;

    public MessageRead() {}

    public MessageRead(ApplicationUser user, Message message, LocalDateTime readAt) {
        this.user = user;
        this.message = message;
        this.readAt = readAt;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

}
