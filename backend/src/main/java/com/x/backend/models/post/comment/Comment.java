package com.x.backend.models.post.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.backend.models.post.Post;
import com.x.backend.models.user.user.ApplicationUser;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(name = "idx_comment_post", columnList = "post_id"),
                @Index(name = "idx_comment_author", columnList = "author_id"),
                @Index(name = "idx_comment_created_at", columnList = "created_at")
        }
)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnore
    private ApplicationUser author;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SubComment> subComments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<ApplicationUser> likes = new HashSet<>();

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "edited_at")
    private LocalDateTime editedAt;

    public Comment() {}

    public Comment(Long id,
                   Post post,
                   ApplicationUser author,
                   String content,
                   Set<SubComment> subComments,
                   Set<ApplicationUser> likes,
                   boolean isDeleted,
                   LocalDateTime createdAt,
                   LocalDateTime editedAt
    ) {
        this.id = id;
        this.post = post;
        this.author = author;
        this.content = content;
        this.subComments = subComments;
        this.likes = likes;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    public Set<SubComment> getSubComments() {
        return subComments;
    }

    public void setSubComments(Set<SubComment> subComments) {
        this.subComments = subComments;
    }

    public Set<ApplicationUser> getLikes() {
        return likes;
    }

    public void setLikes(Set<ApplicationUser> likes) {
        this.likes = likes;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

}
