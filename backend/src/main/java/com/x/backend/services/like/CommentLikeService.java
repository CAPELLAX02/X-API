package com.x.backend.services.like;

import com.x.backend.utils.api.BaseApiResponse;

public interface CommentLikeService {

    BaseApiResponse<String> likeComment(String authenticatedUsername, Long commentId);
    BaseApiResponse<String> unlikeComment(String authenticatedUsername, Long commentId);
    BaseApiResponse<Long> countCommentLikes(Long commentId);

}
