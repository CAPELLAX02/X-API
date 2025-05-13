package com.x.backend.models.post;

import com.x.backend.models.post.comment.Comment;
import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.models.post.poll.Post;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "likes",
        indexes = {
                @Index(name = "idx_like_user", columnList = "user_id"),
                @Index(name = "idx_like_post", columnList = "post_id"),
                @Index(name = "idx_like_comment", columnList = "comment_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_post_like",
                        columnNames = {
                                "user_id",
                                "post_id"
                        }
                ),
                @UniqueConstraint(
                        name = "uq_user_comment_like",
                        columnNames = {
                                "user_id",
                                "comment_id"
                        }
                )
        }
)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Like() {}

    public Like(Long id, ApplicationUser user, Post post, Comment comment, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.comment = comment;
        this.createdAt = createdAt;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
