package com.x.backend.services.comment;

import com.x.backend.dto.comment.request.CreateCommentRequest;
import com.x.backend.dto.comment.request.EditCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.utils.api.BaseApiResponse;

import java.util.List;

/**
 * Service for managing comments on posts (top-level).
 */
public interface PostCommentService {

    /**
     * Creates a top-level comment on a given post.
     *
     * @param username Author's username
     * @param postId ID of the post
     * @param req Request containing the comment content
     * @return Response containing the created comment
     */
    BaseApiResponse<CommentResponse> createComment(String username, Long postId, CreateCommentRequest req);

    /**
     * Edits an existing comment.
     *
     * @param username Requesting user
     * @param commentId ID of the comment to edit
     * @param req New content payload
     * @return Response containing the updated comment
     */
    BaseApiResponse<CommentResponse> editComment(String username, Long commentId, EditCommentRequest req);

    /**
     * Soft-deletes a comment.
     *
     * @param username Requesting user
     * @param commentId ID of the comment
     * @return Confirmation response
     */
    BaseApiResponse<String> deleteComment(String username, Long commentId);

    /**
     * Returns all top-level comments on a post.
     *
     * @param postId Post ID
     * @return List of top-level comments
     */
    BaseApiResponse<List<CommentResponse>> getCommentsByPost(Long postId);

}
