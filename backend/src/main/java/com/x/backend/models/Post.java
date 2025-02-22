package com.x.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.backend.models.enums.Audience;
import com.x.backend.models.enums.ReplyRestriction;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
        name = "posts",
        indexes =  {
                @Index(name = "idx_post_author", columnList = "author_id"),
                @Index(name = "idx_post_date", columnList = "posted_date"),
                @Index(name = "idx_post_reply_to", columnList = "reply_to")
        }
)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Integer postId;

    @Column(name = "content", length=256, nullable=false)
    private String content;

    @Column(name="posted_date", nullable=false)
    private LocalDateTime postedDate;

    @Column(name="is_reply")
    private Boolean reply;

    @Column(name="reply_to")
    private Integer replyTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id", nullable=false)
    private ApplicationUser author;

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="post_likes_junction",
            joinColumns = { @JoinColumn(name="post_id") },
            inverseJoinColumns = { @JoinColumn(name="user_id") }
    )
    private Set<ApplicationUser> likes = new HashSet<>();

    @OneToMany
    private List<Image> images = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="post_reply_junction",
            joinColumns= { @JoinColumn(name="post_id") },
            inverseJoinColumns = { @JoinColumn(name="reply_id") }
    )
    private Set<Post> replies = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="post_repost_junction",
            joinColumns = { @JoinColumn(name="post_id") },
            inverseJoinColumns = { @JoinColumn(name="user_id") }
    )
    private Set<ApplicationUser> reposts = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="post_bookmark_junction",
            joinColumns= { @JoinColumn(name="post_id") },
            inverseJoinColumns = { @JoinColumn(name="user_id") }
    )
    private Set<ApplicationUser> bookmarks = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="post_view_junction",
            joinColumns= { @JoinColumn(name="post_id") },
            inverseJoinColumns = { @JoinColumn(name="user_id") }
    )
    private Set<ApplicationUser> views = new HashSet<>();

    @Column(name = "scheduled")
    private Boolean scheduled;

    @Column(name="scheduled_date")
    private LocalDateTime scheduledDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "audience")
    private Audience audience;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="reply_restriction")
    private ReplyRestriction replyRestriction;

    public Post() {}

    public ApplicationUser getAuthor() {
        return author;
    }

    public void setAuthor(ApplicationUser author) {
        this.author = author;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public Boolean getReply() {
        return reply;
    }

    public void setReply(Boolean reply) {
        this.reply = reply;
    }

    public Integer getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Integer replyTo) {
        this.replyTo = replyTo;
    }

    public Set<ApplicationUser> getLikes() {
        return likes;
    }

    public void setLikes(Set<ApplicationUser> likes) {
        this.likes = likes;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Set<Post> getReplies() {
        return replies;
    }

    public void setReplies(Set<Post> replies) {
        this.replies = replies;
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

    public Boolean getScheduled() {
        return scheduled;
    }

    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Post post)) return false;
        return Objects.equals(getPostId(), post.getPostId())
                && Objects.equals(getContent(), post.getContent())
                && Objects.equals(getPostedDate(), post.getPostedDate())
                && Objects.equals(getReply(), post.getReply())
                && Objects.equals(getReplyTo(), post.getReplyTo())
                && Objects.equals(getAuthor(), post.getAuthor())
                && Objects.equals(getLikes(), post.getLikes())
                && Objects.equals(getImages(), post.getImages())
                && Objects.equals(getReplies(), post.getReplies())
                && Objects.equals(getReposts(), post.getReposts())
                && Objects.equals(getBookmarks(), post.getBookmarks())
                && Objects.equals(getViews(), post.getViews())
                && Objects.equals(getScheduled(), post.getScheduled())
                && Objects.equals(getScheduledDate(), post.getScheduledDate())
                && getAudience() == post.getAudience()
                && getReplyRestriction() == post.getReplyRestriction();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getPostId(),
                getContent(),
                getPostedDate(),
                getReply(),
                getReplyTo(),
                getAuthor(),
                getLikes(),
                getImages(),
                getReplies(),
                getReposts(),
                getBookmarks(),
                getViews(),
                getScheduled(),
                getScheduledDate(),
                getAudience(),
                getReplyRestriction()
        );
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", content='" + content + '\'' +
                ", postedDate=" + postedDate +
                ", reply=" + reply +
                ", replyTo=" + replyTo +
                ", author=" + author +
                ", likes=" + likes +
                ", images=" + images +
                ", replies=" + replies +
                ", reposts=" + reposts +
                ", bookmarks=" + bookmarks +
                ", views=" + views +
                ", scheduled=" + scheduled +
                ", scheduledDate=" + scheduledDate +
                ", audience=" + audience +
                ", replyRestriction=" + replyRestriction +
                '}';
    }

}
