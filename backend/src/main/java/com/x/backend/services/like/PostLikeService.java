package com.x.backend.services.like;

import com.x.backend.utils.api.BaseApiResponse;

public interface PostLikeService {

    BaseApiResponse<String> likePost(String username, Long postId);
    BaseApiResponse<String> unlikePost(String username, Long postId);
    BaseApiResponse<Long> getLikeCount(Long postId);

}
