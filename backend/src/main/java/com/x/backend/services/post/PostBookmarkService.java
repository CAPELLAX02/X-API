package com.x.backend.services.post;

import com.x.backend.utils.api.BaseApiResponse;

public interface PostBookmarkService {

    BaseApiResponse<String> bookmarkPost(String username, Long postId);
    BaseApiResponse<String> removeBookmark(String username, Long postId);
    BaseApiResponse<Boolean> hasUserBookmarked(String username, Long postId);
    BaseApiResponse<Long> getBookmarkCount(Long postId);

}
