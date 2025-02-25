package com.x.backend.models.entities;

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
public class MessageRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_read_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "read_at", nullable = false)
    private LocalDateTime readAt;

    public MessageRead() {}

    public MessageRead(Long id, ApplicationUser user, Message message, LocalDateTime readAt) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.readAt = readAt;
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
