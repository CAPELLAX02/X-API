package com.x.backend.services.post;

import com.x.backend.dto.post.request.CreatePostRequest;
import com.x.backend.dto.post.response.PostResponse;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    BaseApiResponse<PostResponse> createPost(
            String username,
            CreatePostRequest req,
            List<MultipartFile> postImages
    );

    BaseApiResponse<PostResponse> getPostByIdWithAccessControl(Long postId, String currentUsername);

    BaseApiResponse<List<PostResponse>> getPostsByAuthor(String author);

    BaseApiResponse<List<PostResponse>> getTimeline(String currentUsername);

    BaseApiResponse<Long> getUserPostCount(String username);

}
