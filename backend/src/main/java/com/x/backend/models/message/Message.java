package com.x.backend.models.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.backend.models.image.Image;
import com.x.backend.models.user.ApplicationUser;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "messages",
        indexes = {
                @Index(name = "idx_message_conversation", columnList = "conversation_id"),
                @Index(name = "idx_message_sender", columnList = "sender_id"),
                @Index(name = "idx_message_read", columnList = "is_read")
        }
)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    @JsonIgnore
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnore
    private ApplicationUser sender;

    @Column(name = "content", length = 2000)
    private String content;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_attachment", referencedColumnName = "image_id")
    private Image mediaAttachment;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "is_deleted_for_sender", nullable = false)
    private boolean isDeletedForSender = false;

    @Column(name = "is_deleted_for_receiver", nullable = false)
    private boolean isDeletedForReceiver = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to")
    @JsonIgnore
    private Message replyTo;

    @CreationTimestamp
    @Column(name = "sent_at", nullable = false, updatable = false)
    private LocalDateTime sentAt;

    public Message() {}

    public Message(Long id,
                   Conversation conversation,
                   ApplicationUser sender,
                   String content,
                   Image mediaAttachment,
                   boolean isRead,
                   boolean isDeletedForSender,
                   boolean isDeletedForReceiver,
                   Message replyTo,
                   LocalDateTime sentAt
    ) {
        this.id = id;
        this.conversation = conversation;
        this.sender = sender;
        this.content = content;
        this.mediaAttachment = mediaAttachment;
        this.isRead = isRead;
        this.isDeletedForSender = isDeletedForSender;
        this.isDeletedForReceiver = isDeletedForReceiver;
        this.replyTo = replyTo;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public ApplicationUser getSender() {
        return sender;
    }

    public void setSender(ApplicationUser sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Image getMediaAttachment() {
        return mediaAttachment;
    }

    public void setMediaAttachment(Image mediaAttachment) {
        this.mediaAttachment = mediaAttachment;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isDeletedForSender() {
        return isDeletedForSender;
    }

    public void setDeletedForSender(boolean deletedForSender) {
        isDeletedForSender = deletedForSender;
    }

    public boolean isDeletedForReceiver() {
        return isDeletedForReceiver;
    }

    public void setDeletedForReceiver(boolean deletedForReceiver) {
        isDeletedForReceiver = deletedForReceiver;
    }

    public Message getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Message replyTo) {
        this.replyTo = replyTo;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

}
