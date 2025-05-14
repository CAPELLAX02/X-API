package com.x.backend.controllers;

import com.x.backend.dto.comment.request.CreateSubCommentRequest;
import com.x.backend.dto.comment.response.SubCommentResponse;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.services.comment.PostSubCommentService;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/comments/{commentId}/replies")
public class PostSubCommentController {

    private final PostSubCommentService postSubCommentService;

    public PostSubCommentController(PostSubCommentService postSubCommentService) {
        this.postSubCommentService = postSubCommentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<SubCommentResponse>> createSubComment(
            @PathVariable Long postId,
            @PathVariable Long parentCommentId,
            @Valid @RequestBody CreateSubCommentRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<SubCommentResponse> res = postSubCommentService.createSubComment(user.getUsername(), postId, parentCommentId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
