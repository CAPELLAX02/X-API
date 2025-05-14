package com.x.backend.controllers;

import com.x.backend.dto.comment.request.EditSubCommentRequest;
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
@RequestMapping("/sub-comments")
public class SubCommentController {

    private final PostSubCommentService postSubCommentService;

    public SubCommentController(PostSubCommentService postSubCommentService) {
        this.postSubCommentService = postSubCommentService;
    }

    @PutMapping("/{subCommentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<SubCommentResponse>> editSubComment(
            @PathVariable Long subCommentId,
            @Valid @RequestBody EditSubCommentRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<SubCommentResponse> res = postSubCommentService.editSubComment(user.getUsername(), subCommentId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/{subCommentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> deleteSubComment(
            @PathVariable Long subCommentId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = postSubCommentService.deleteSubComment(user.getUsername(), subCommentId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
