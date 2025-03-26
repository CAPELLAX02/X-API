package com.x.backend.services.post;

import com.x.backend.dto.post.request.CreatePostRequest;
import com.x.backend.dto.post.response.PostResponse;
import com.x.backend.utils.api.BaseApiResponse;

public interface PostService {

    BaseApiResponse<PostResponse> createPost(String username, CreatePostRequest req);

}
