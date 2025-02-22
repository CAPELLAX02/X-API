package com.x.backend.controllers;

import com.x.backend.dto.CreatePostRequest;
import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Post;
import com.x.backend.services.post.PostService;
import com.x.backend.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin("*")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody CreatePostRequest createPostRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(createPostRequest));
    }

    @GetMapping("/author/{userId}")
    public ResponseEntity<List<Post>> getAllPostsByAuthor(@PathVariable Integer userId) {
        ApplicationUser author = userService.getUserById(userId);
        return ResponseEntity.ok(postService.getAllPostsByAuthor(author));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

}
