package com.x.backend.models.entities;

import com.x.backend.models.AbstractBaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "conversations",
        indexes = {
                @Index(
                        name = "idx_conversation_last_message",
                        columnList = "last_message_sent_at"
                ),
                @Index(
                        name = "idx_conversation_created_at",
                        columnList = "created_at"
                )
        }
)
public class Conversation extends AbstractBaseEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "conversation_participants",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<ApplicationUser> participants = new HashSet<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages = new HashSet<>();

    @Column(name = "is_group_chat", nullable = false)
    private boolean isGroupChat = false;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_message_sent_at")
    private LocalDateTime lastMessageSentAt;

    public Conversation() {}

    public Conversation(
            Set<ApplicationUser> participants,
            Set<Message> messages,
            boolean isGroupChat,
            String groupName,
            LocalDateTime createdAt,
            LocalDateTime lastMessageSentAt
    ) {
        this.participants = participants;
        this.messages = messages;
        this.isGroupChat = isGroupChat;
        this.groupName = groupName;
        this.createdAt = createdAt;
        this.lastMessageSentAt = lastMessageSentAt;
    }

    public Set<ApplicationUser> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<ApplicationUser> participants) {
        this.participants = participants;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public boolean isGroupChat() {
        return isGroupChat;
    }

    public void setGroupChat(boolean groupChat) {
        isGroupChat = groupChat;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastMessageSentAt() {
        return lastMessageSentAt;
    }

    public void setLastMessageSentAt(LocalDateTime lastMessageSentAt) {
        this.lastMessageSentAt = lastMessageSentAt;
    }

}
