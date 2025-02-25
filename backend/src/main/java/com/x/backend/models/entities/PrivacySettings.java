package com.x.backend.models.entities;

import com.x.backend.models.enums.PrivacyLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "user_privacy_settings")
public class PrivacySettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privacy_settings_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_privacy", nullable = false, length = 20)
    private PrivacyLevel messagePrivacy = PrivacyLevel.EVERYONE;

    @Enumerated(EnumType.STRING)
    @Column(name = "mention_privacy", nullable = false, length = 20)
    private PrivacyLevel mentionPrivacy = PrivacyLevel.EVERYONE;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_visibility", nullable = false, length = 20)
    private PrivacyLevel postVisibility = PrivacyLevel.EVERYONE;

    public PrivacySettings() {}

    public PrivacySettings(
            Long id,
            ApplicationUser user,
            PrivacyLevel messagePrivacy,
            PrivacyLevel mentionPrivacy,
            PrivacyLevel postVisibility
    ) {
        this.id = id;
        this.user = user;
        this.messagePrivacy = messagePrivacy;
        this.mentionPrivacy = mentionPrivacy;
        this.postVisibility = postVisibility;
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

    public PrivacyLevel getMessagePrivacy() {
        return messagePrivacy;
    }

    public void setMessagePrivacy(PrivacyLevel messagePrivacy) {
        this.messagePrivacy = messagePrivacy;
    }

    public PrivacyLevel getMentionPrivacy() {
        return mentionPrivacy;
    }

    public void setMentionPrivacy(PrivacyLevel mentionPrivacy) {
        this.mentionPrivacy = mentionPrivacy;
    }

    public PrivacyLevel getPostVisibility() {
        return postVisibility;
    }

    public void setPostVisibility(PrivacyLevel postVisibility) {
        this.postVisibility = postVisibility;
    }

}
