package com.x.backend.controllers;

import com.x.backend.dto.post.request.CreatePostRequest;
import com.x.backend.dto.post.response.PostResponse;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.post.PostService;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<PostResponse>> createPost(
            @AuthenticationPrincipal ApplicationUser user,
            @RequestBody @Valid CreatePostRequest req
    ) {
        String username = user.getUsername();
        BaseApiResponse<PostResponse> res = postService.createPost(username, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
