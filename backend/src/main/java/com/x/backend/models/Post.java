package com.x.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
        name = "posts",
        indexes = {
                @Index(name = "idx_post_author", columnList = "author_id"),
                @Index(name = "idx_post_privacy", columnList = "privacy_level"),
                @Index(name = "idx_post_created_at", columnList = "created_at"),
                @Index(name = "idx_post_scheduled", columnList = "scheduled, scheduled_date")
        }
)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnore
    private ApplicationUser author;

    @OneToMany(mappedBy = "replyTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> replies = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<ApplicationUser> likes = new HashSet<>();

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

    @Column(name = "scheduled", nullable = false)
    private boolean scheduled = false;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "reply_restriction", nullable = false, length = 20)
    private ReplyRestriction replyRestriction = ReplyRestriction.EVERYONE;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    public Post() {}

    public Post(
            Long id,
            String content,
            LocalDateTime createdAt,
            boolean isReply,
            Post replyTo,
            ApplicationUser author,
            Set<Post> replies,
            Set<ApplicationUser> likes,
            Set<ApplicationUser> reposts,
            Set<ApplicationUser> bookmarks,
            Set<ApplicationUser> views,
            List<Image> mediaAttachments,
            boolean scheduled,
            LocalDateTime scheduledDate,
            ReplyRestriction replyRestriction,
            Poll poll
    ) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.isReply = isReply;
        this.replyTo = replyTo;
        this.author = author;
        this.replies = replies;
        this.likes = likes;
        this.reposts = reposts;
        this.bookmarks = bookmarks;
        this.views = views;
        this.mediaAttachments = mediaAttachments;
        this.scheduled = scheduled;
        this.scheduledDate = scheduledDate;
        this.replyRestriction = replyRestriction;
        this.poll = poll;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ApplicationUser getAuthor() {
        return author;
    }

    public void setAuthor(ApplicationUser author) {
        this.author = author;
    }

    public Set<Post> getReplies() {
        return replies;
    }

    public void setReplies(Set<Post> replies) {
        this.replies = replies;
    }

    public Set<ApplicationUser> getLikes() {
        return likes;
    }

    public void setLikes(Set<ApplicationUser> likes) {
        this.likes = likes;
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

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", isReply=" + isReply +
                ", replyTo=" + replyTo +
                ", author=" + author +
                ", replies=" + replies +
                ", likes=" + likes +
                ", reposts=" + reposts +
                ", bookmarks=" + bookmarks +
                ", views=" + views +
                ", mediaAttachments=" + mediaAttachments +
                ", scheduled=" + scheduled +
                ", scheduledDate=" + scheduledDate +
                ", replyRestriction=" + replyRestriction +
                ", poll=" + poll +
                '}';
    }

}
