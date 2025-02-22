package com.x.backend.services.post;

import com.x.backend.dto.CreatePostRequest;
import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Post;

import java.util.List;

public interface PostService {

    Post createPost(CreatePostRequest createPostRequest);
    List<Post> getAllPosts();
    Post getPostById(Integer id);
    List<Post> getAllPostsByAuthor(ApplicationUser author);
    void deletePost(Integer id);

}
