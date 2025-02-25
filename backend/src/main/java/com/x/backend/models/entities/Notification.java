package com.x.backend.models.entities;

import com.x.backend.models.AbstractBaseEntity;
import com.x.backend.models.enums.NotificationType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_notification_user", columnList = "recipient_id"),
                @Index(name = "idx_notification_type", columnList = "type"),
                @Index(name = "idx_notification_read", columnList = "is_read")
        }
)
public class Notification extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private ApplicationUser recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triggered_by")
    private ApplicationUser triggeredBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private NotificationType type;

    @Column(name = "message_template", nullable = false)
    private String messageTemplate;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Notification() {}

    public Notification(
            ApplicationUser recipient,
            ApplicationUser triggeredBy,
            NotificationType type,
            String messageTemplate,
            boolean isRead,
            LocalDateTime createdAt
    ) {
        this.recipient = recipient;
        this.triggeredBy = triggeredBy;
        this.type = type;
        this.messageTemplate = messageTemplate;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public ApplicationUser getRecipient() {
        return recipient;
    }

    public void setRecipient(ApplicationUser recipient) {
        this.recipient = recipient;
    }

    public ApplicationUser getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(ApplicationUser triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
