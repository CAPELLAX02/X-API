package com.x.backend.controllers;

import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.post.PostBookmarkService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostBookmarkController {

    private final PostBookmarkService postBookmarkService;

    public PostBookmarkController(PostBookmarkService postBookmarkService) {
        this.postBookmarkService = postBookmarkService;
    }

    @PostMapping("/{postId}/bookmark")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> bookmarkPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = postBookmarkService.bookmarkPost(user.getUsername(), postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/{postId}/bookmark")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> removeBookmark(
            @PathVariable Long postId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = postBookmarkService.removeBookmark(user.getUsername(), postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/{postId}/bookmark/status")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<Boolean>> hasUserBookmarked(
            @PathVariable Long postId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<Boolean> res = postBookmarkService.hasUserBookmarked(user.getUsername(), postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/{postId}/bookmark/count")
    public ResponseEntity<BaseApiResponse<Long>> getBookmarkCount(@PathVariable Long postId) {
        BaseApiResponse<Long> res = postBookmarkService.getBookmarkCount(postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
