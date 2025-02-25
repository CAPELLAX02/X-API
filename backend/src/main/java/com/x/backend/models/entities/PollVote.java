package com.x.backend.models.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "poll_votes",
        indexes = {
                @Index(name = "idx_poll_vote_poll", columnList = "poll_id"),
                @Index(name = "idx_poll_vote_user", columnList = "user_id"),
                @Index(name = "idx_poll_vote_option", columnList = "option_index")
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

    @Column(name = "option_index", nullable = false)
    private int optionIndex;

    public PollVote() {}

    public PollVote(Long id, Poll poll, ApplicationUser user, int optionIndex) {
        this.id = id;
        this.poll = poll;
        this.user = user;
        this.optionIndex = optionIndex;
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

    public int getOptionIndex() {
        return optionIndex;
    }

    public void setOptionIndex(int optionIndex) {
        this.optionIndex = optionIndex;
    }

}
