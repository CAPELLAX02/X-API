package com.x.backend.models.post.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.backend.models.post.poll.Post;
import com.x.backend.models.user.user.ApplicationUser;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "sub_comments",
        indexes = {
                @Index(name = "idx_subcomment_post", columnList = "post_id"),
                @Index(name = "idx_subcomment_author", columnList = "author_id"),
                @Index(name = "idx_subcomment_parent_comment", columnList = "parent_comment_id"),
                @Index(name = "idx_subcomment_created_at", columnList = "created_at")
        }
)
public class SubComment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id", nullable = false)
        @JsonIgnore
        private Post post;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "author_id", nullable = false)
        @JsonIgnore
        private ApplicationUser author;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_comment_id", nullable = false)
        @JsonIgnore
        private Comment parentComment;

        @Column(name = "content", nullable = false, length = 500)
        private String content;

        @ManyToMany
        @JoinTable(
                name = "sub_comment_likes",
                joinColumns = @JoinColumn(name = "sub_comment_id"),
                inverseJoinColumns = @JoinColumn(name = "user_id")
        )
        private Set<ApplicationUser> likes = new HashSet<>();

        @Column(name = "is_deleted", nullable = false)
        private boolean isDeleted = false;

        @CreationTimestamp
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime editedAt;

        public SubComment() {}

        public SubComment(Long id,
                          Post post,
                          ApplicationUser author,
                          Comment parentComment,
                          String content,
                          Set<ApplicationUser> likes,
                          boolean isDeleted,
                          LocalDateTime createdAt,
                          LocalDateTime editedAt
        ) {
                this.id = id;
                this.post = post;
                this.author = author;
                this.parentComment = parentComment;
                this.content = content;
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

        public Comment getParentComment() {
                return parentComment;
        }

        public void setParentComment(Comment parentComment) {
                this.parentComment = parentComment;
        }

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
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
