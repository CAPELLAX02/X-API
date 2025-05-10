package com.x.backend.services.comment;

import com.x.backend.dto.comment.request.CreateCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.utils.api.BaseApiResponse;

import java.util.List;

public interface CommentService {

    BaseApiResponse<CommentResponse> createComment(String username, Long postId, CreateCommentRequest req);
    BaseApiResponse<List<CommentResponse>> getCommentsByPost(Long postId);

}
