package com.x.backend.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.backend.models.AbstractBaseEntity;
import com.x.backend.models.enums.Audience;
import com.x.backend.models.enums.ReplyRestriction;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
        name = "posts",
        indexes = {
                @Index(name = "idx_post_author", columnList = "author_id"),
                @Index(name = "idx_post_created_at", columnList = "created_at"),
                @Index(name = "idx_post_scheduled", columnList = "scheduled, scheduled_date")
        }
)
public class Post extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnore
    private ApplicationUser author;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_reply", nullable = false)
    private boolean isReply = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to", referencedColumnName = "post_id")
    @JsonIgnore
    private Post replyTo;

    @OneToMany(mappedBy = "replyTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> replies = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_reposts",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<ApplicationUser> reposts = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_bookmarks",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<ApplicationUser> bookmarks = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_views",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<ApplicationUser> views = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private List<Image> mediaAttachments = new ArrayList<>();

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "audience", nullable = false, length = 20)
    private Audience audience = Audience.EVERYONE;

    @Enumerated(EnumType.STRING)
    @Column(name = "reply_restriction", nullable = false, length = 20)
    private ReplyRestriction replyRestriction = ReplyRestriction.EVERYONE;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    public Post() {}

    public Post(
            ApplicationUser author,
            String content,
            LocalDateTime createdAt,
            boolean isReply,
            Post replyTo,
            Set<Post> replies,
            Set<Like> likes,
            Set<Comment> comments,
            Set<ApplicationUser> reposts,
            Set<ApplicationUser> bookmarks,
            Set<ApplicationUser> views,
            List<Image> mediaAttachments,
            LocalDateTime scheduledDate,
            Audience audience,
            ReplyRestriction replyRestriction,
            Poll poll
    ) {
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.isReply = isReply;
        this.replyTo = replyTo;
        this.replies = replies;
        this.likes = likes;
        this.comments = comments;
        this.reposts = reposts;
        this.bookmarks = bookmarks;
        this.views = views;
        this.mediaAttachments = mediaAttachments;
        this.scheduledDate = scheduledDate;
        this.audience = audience;
        this.replyRestriction = replyRestriction;
        this.poll = poll;
    }

    public ApplicationUser getAuthor() {
        return author;
    }

    public void setAuthor(ApplicationUser author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    public Post getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Post replyTo) {
        this.replyTo = replyTo;
    }

    public Set<Post> getReplies() {
        return replies;
    }

    public void setReplies(Set<Post> replies) {
        this.replies = replies;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<ApplicationUser> getReposts() {
        return reposts;
    }

    public void setReposts(Set<ApplicationUser> reposts) {
        this.reposts = reposts;
    }

    public Set<ApplicationUser> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Set<ApplicationUser> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public Set<ApplicationUser> getViews() {
        return views;
    }

    public void setViews(Set<ApplicationUser> views) {
        this.views = views;
    }

    public List<Image> getMediaAttachments() {
        return mediaAttachments;
    }

    public void setMediaAttachments(List<Image> mediaAttachments) {
        this.mediaAttachments = mediaAttachments;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public ReplyRestriction getReplyRestriction() {
        return replyRestriction;
    }

    public void setReplyRestriction(ReplyRestriction replyRestriction) {
        this.replyRestriction = replyRestriction;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

}
