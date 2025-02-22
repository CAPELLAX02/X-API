package com.x.backend.services.post;

import com.x.backend.dto.CreatePostRequest;
import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    Post createPost(CreatePostRequest createPostRequest);
    Post createMediaPost(String postJson, List<MultipartFile> files);
    List<Post> getAllPosts();
    Post getPostById(Integer id);
    List<Post> getAllPostsByAuthor(ApplicationUser author);
    void deletePost(Integer id);

}
