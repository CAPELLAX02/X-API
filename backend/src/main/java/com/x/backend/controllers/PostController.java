package com.x.backend.controllers;

import com.x.backend.dto.post.request.CreatePostRequest;
import com.x.backend.dto.post.response.PostResponse;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.post.PostService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseApiResponse<PostResponse>> createPost(
            @RequestPart("post") CreatePostRequest req,
            @RequestPart(value = "postImages", required = false) List<MultipartFile> postImages,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        String username = user.getUsername();
        return ResponseEntity.ok(postService.createPost(username, req, postImages));
    }

}
