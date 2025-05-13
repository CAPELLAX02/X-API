package com.x.backend.controllers;

import com.x.backend.dto.poll.request.PollVoteRequest;
import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.services.poll.PostPollService;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<BaseApiResponse<String>> voteInPoll(
            @PathVariable Long postId,
            @Valid @RequestBody PollVoteRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = postPollService.voteInPoll(user.getUsername(), postId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/revoke")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> revokePollVote(
            @PathVariable Long postId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = postPollService.revokePollVote(user.getUsername(), postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
