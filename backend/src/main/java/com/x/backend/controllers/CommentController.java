package com.x.backend.controllers;

import com.x.backend.dto.comment.request.CreateCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.comment.CommentService;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // TODO: Add @Valid annotation every required endpoint requests...

    @PostMapping("/post/{postId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        String username = user.getUsername();
        BaseApiResponse<CommentResponse> res = commentService.createComment(username, postId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<BaseApiResponse<List<CommentResponse>>> getCommentsByPost(@PathVariable Long postId) {
        BaseApiResponse<List<CommentResponse>> res = commentService.getCommentsByPost(postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
