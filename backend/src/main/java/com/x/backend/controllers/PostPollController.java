package com.x.backend.controllers;

import com.x.backend.dto.poll.request.PollVoteRequest;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.poll.PollVoteService;
import com.x.backend.services.poll.PostPollService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/poll")
public class PostPollController {

    private final PostPollService postPollService;

    public PostPollController(PostPollService postPollService) {
        this.postPollService = postPollService;
    }

    @PostMapping("/vote")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> voteOnPoll(
            @PathVariable Long postId,
            @RequestBody PollVoteRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        String username = user.getUsername();
        BaseApiResponse<String> res = postPollService.voteInPoll(username, postId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
