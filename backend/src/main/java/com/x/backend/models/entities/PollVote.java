package com.x.backend.models.entities;

import com.x.backend.models.AbstractBaseEntity;
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
public class PollVote extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @Column(name = "option_index", nullable = false)
    private int optionIndex;

    public PollVote() {}

    public PollVote(Poll poll, ApplicationUser user, int optionIndex) {
        this.poll = poll;
        this.user = user;
        this.optionIndex = optionIndex;
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
