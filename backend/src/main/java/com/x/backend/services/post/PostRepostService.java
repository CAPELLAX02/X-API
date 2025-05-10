package com.x.backend.services.post;

import com.x.backend.utils.api.BaseApiResponse;

public interface PostRepostService {

    BaseApiResponse<String> repostPost(String username, Long postId);
    BaseApiResponse<String> undoRepost(String username, Long postId);
    BaseApiResponse<Long> getRepostCount(Long postId);
    BaseApiResponse<Boolean> hasUserReposted(String username, Long postId);

}
