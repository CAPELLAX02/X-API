package com.x.backend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "polls")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Integer pollId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "poll")
    @Column(name = "choices")
    private List<PollChoice> pollChoices;

    public Poll() {}

    public Poll(Integer pollId, LocalDateTime endDate, List<PollChoice> pollChoices) {
        this.pollId = pollId;
        this.endDate = endDate;
        this.pollChoices = pollChoices;
    }

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<PollChoice> getPollChoices() {
        return pollChoices;
    }

    public void setPollChoices(List<PollChoice> pollChoices) {
        this.pollChoices = pollChoices;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Poll poll)) return false;
        return Objects.equals(getPollId(), poll.getPollId())
                && Objects.equals(getEndDate(), poll.getEndDate())
                && Objects.equals(getPollChoices(), poll.getPollChoices());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getPollId(),
                getEndDate(),
                getPollChoices()
        );
    }

    @Override
    public String toString() {
        return "Poll{" +
                "pollId=" + pollId +
                ", endDate=" + endDate +
                ", pollChoices=" + pollChoices +
                '}';
    }
}
