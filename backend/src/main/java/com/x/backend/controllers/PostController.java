package com.x.backend.controllers;

import com.x.backend.dto.post.request.CreatePostRequest;
import com.x.backend.dto.post.response.PostResponse;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.post.PostService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
        BaseApiResponse<PostResponse> res = postService.createPost(username, req, postImages);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<BaseApiResponse<PostResponse>> getPostById(@PathVariable Long postId) {
        BaseApiResponse<PostResponse> res = postService.getPostById(postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/author/{username}")
    public ResponseEntity<BaseApiResponse<List<PostResponse>>> getPostsByAuthor(@PathVariable String username) {
        BaseApiResponse<List<PostResponse>> res = postService.getPostsByAuthor(username);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/timeline")
    public ResponseEntity<BaseApiResponse<List<PostResponse>>> getPostTimeline(
            @AuthenticationPrincipal ApplicationUser currentUser
    ) {
        String currentUsername = currentUser.getUsername();
        BaseApiResponse<List<PostResponse>> res = postService.getTimeline(currentUsername);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/user/{username}/post-count")
    public ResponseEntity<BaseApiResponse<Long>> getUserPostCount(@PathVariable String username) {
        BaseApiResponse<Long> res = postService.getUserPostCount(username);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
