package com.x.backend.models.entities;

import com.x.backend.models.AbstractBaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "poll_options")
public class PollOption extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @Column(name = "option_text", nullable = false, length = 100)
    private String optionText;

    public PollOption() {}

    public PollOption(Poll poll, String optionText) {
        this.poll = poll;
        this.optionText = optionText;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

}
