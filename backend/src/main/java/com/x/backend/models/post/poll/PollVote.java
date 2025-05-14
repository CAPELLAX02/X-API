package com.x.backend.models.post.poll;

import com.x.backend.models.user.ApplicationUser;
import jakarta.persistence.*;

@Entity
@Table(
        name = "poll_votes",
        indexes = {
                @Index(name = "idx_poll_vote_poll", columnList = "poll_id"),
                @Index(name = "idx_poll_vote_user", columnList = "user_id"),
                @Index(name = "idx_poll_vote_option", columnList = "option_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_poll_vote_user",
                        columnNames = {
                                "poll_id",
                                "user_id"
                        }
                )
        }
)
public class PollVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_vote_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private PollOption option;

    public PollVote() {}

    public PollVote(Long id, Poll poll, ApplicationUser user, PollOption option) {
        this.id = id;
        this.poll = poll;
        this.user = user;
        this.option = option;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public PollOption getOption() {
        return option;
    }

    public void setOption(PollOption option) {
        this.option = option;
    }

}
