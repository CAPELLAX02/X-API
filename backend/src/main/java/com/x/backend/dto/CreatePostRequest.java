package com.x.backend.dto;

import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Post;
import com.x.backend.models.enums.Audience;
import com.x.backend.models.enums.ReplyRestriction;

import java.time.LocalDateTime;
import java.util.Set;

public record CreatePostRequest(


        String content,


        ApplicationUser author,


        Set<Post> replies,


        Boolean scheduled,


        LocalDateTime scheduledDate,


        Audience audience,


        ReplyRestriction replyRestriction

) {
}
