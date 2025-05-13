package com.x.backend.models.post.enums;

public enum ReplyRestriction {

    EVERYONE,       // Anyone can reply
    FOLLOWERS_ONLY, // Only followers can reply
    MENTIONED_ONLY, // Only mentioned users can reply
    NO_ONE          // Replies disabled

}
