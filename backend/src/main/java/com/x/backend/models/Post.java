package com.x.backend.models;

import com.x.backend.models.enums.Audience;
import com.x.backend.models.enums.ReplyRestriction;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "content", nullable = false, length = 256)
    private String content;

    @Column(name = "posted_date")
    private LocalDateTime postedDate;

    @Column(name = "is_reply", nullable = true)
    private Boolean isReply;

    @Column(name = "reply_to")
    private Integer replyTo;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private ApplicationUser author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_likes_junction",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    @Column(name = "likes")
    private Set<ApplicationUser> likes = new HashSet<>();

    @OneToMany
    @Column(name = "images")
    private List<Image> images = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_reply_junction",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "reply_id") }
    )
    @Column(name = "replies")
    private Set<Post> replies = new HashSet<>();

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="post_repost_junction",
            joinColumns = { @JoinColumn(name="post_id") },
            inverseJoinColumns = { @JoinColumn(name="user_id") }
    )
    @Column(name = "reposts")
    private Set<ApplicationUser> reposts = new HashSet<>();

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="post_bookmark_junction",
            joinColumns= { @JoinColumn(name="post_id") },
            inverseJoinColumns  = { @JoinColumn(name="user_id") }
    )
    @Column(name = "bookmarks")
    private Set<ApplicationUser> bookmarks = new HashSet<>();

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="post_view_junction",
            joinColumns= { @JoinColumn(name="post_id") },
            inverseJoinColumns = { @JoinColumn(name="user_id") }
    )
    @Column(name = "views")
    private Set<ApplicationUser> views = new HashSet<>();

    @Column(name = "scheduled")
    private Boolean scheduled;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "audience")
    private Audience audience;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "reply_restriction")
    private ReplyRestriction replyRestriction;

    public Post() {}

    public Post(
            Integer postId,
            String content,
            LocalDateTime postedDate,
            Boolean reply,
            Integer replyTo,
            ApplicationUser author,
            Set<ApplicationUser> likes,
            List<Image> images,
            Set<Post> replies,
            Set<ApplicationUser> reposts,
            Set<ApplicationUser> bookmarks,
            Set<ApplicationUser> views,
            Boolean scheduled,
            LocalDateTime scheduledDate,
            Audience audience,
            ReplyRestriction replyRestriction
    ) {
        this.postId = postId;
        this.content = content;
        this.postedDate = postedDate;
        this.replyTo = replyTo;
        this.author = author;
        this.likes = likes;
        this.images = images;
        this.replies = replies;
        this.reposts = reposts;
        this.bookmarks = bookmarks;
        this.views = views;
        this.scheduled = scheduled;
        this.scheduledDate = scheduledDate;
        this.audience = audience;
        this.replyRestriction = replyRestriction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public Boolean getReply() {
        return isReply;
    }

    public void setReply(Boolean reply) {
        isReply = reply;
    }

    public Integer getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Integer replyTo) {
        this.replyTo = replyTo;
    }

    public ApplicationUser getAuthor() {
        return author;
    }

    public void setAuthor(ApplicationUser author) {
        this.author = author;
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
                && Objects.equals(isReply, post.isReply)
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
                ", isReply=" + isReply +
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
