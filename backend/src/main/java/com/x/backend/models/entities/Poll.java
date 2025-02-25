package com.x.backend.models.entities;

import com.x.backend.models.AbstractBaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
        name = "polls",
        indexes = {
                @Index(name = "idx_poll_post", columnList = "post_id"),
                @Index(name = "idx_poll_expiration", columnList = "expires_at")
        }
)
public class Poll extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PollOption> options = new ArrayList<>();

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PollVote> votes = new ArrayList<>();

    public Poll() {}

    public Poll(Post post, List<PollOption> options, LocalDateTime expiresAt, List<PollVote> votes) {
        this.post = post;
        this.options = options;
        this.expiresAt = expiresAt;
        this.votes = votes;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<PollOption> getOptions() {
        return options;
    }

    public void setOptions(List<PollOption> options) {
        this.options = options;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public List<PollVote> getVotes() {
        return votes;
    }

    public void setVotes(List<PollVote> votes) {
        this.votes = votes;
    }

}
