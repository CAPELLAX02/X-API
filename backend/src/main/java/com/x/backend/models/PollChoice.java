package com.x.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "poll_choices")
public class PollChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="poll_choice_id")
    private Integer pollChoiceId;

    @ManyToOne
    @JoinColumn(name="poll_id")
    @JsonIgnore
    private Poll poll;

    @Column(name="poll_choice_text")
    private String choiceText;

    @OneToMany
    @Column(name = "votes")
    private Set<ApplicationUser> votes;

    public PollChoice() {}

    public Integer getPollChoiceId() {
        return pollChoiceId;
    }

    public void setPollChoiceId(Integer pollChoiceId) {
        this.pollChoiceId = pollChoiceId;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public Set<ApplicationUser> getVotes() {
        return votes;
    }

    public void setVotes(Set<ApplicationUser> votes) {
        this.votes = votes;
    }

    public PollChoice(
            Integer pollChoiceId,
            Poll poll,
            String choiceText,
            Set<ApplicationUser> votes
    ) {
        this.pollChoiceId = pollChoiceId;
        this.poll = poll;
        this.choiceText = choiceText;
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PollChoice that)) return false;
        return Objects.equals(getPollChoiceId(), that.getPollChoiceId())
                && Objects.equals(getPoll(), that.getPoll())
                && Objects.equals(getChoiceText(), that.getChoiceText())
                && Objects.equals(getVotes(), that.getVotes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getPollChoiceId(),
                getPoll(),
                getChoiceText(),
                getVotes()
        );
    }

    @Override
    public String toString() {
        return "PollChoice{" +
                "pollChoiceId=" + pollChoiceId +
                ", poll=" + poll +
                ", choiceText='" + choiceText + '\'' +
                ", votes=" + votes +
                '}';
    }
    
}
