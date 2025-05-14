package com.x.backend.services.comment;

import com.x.backend.dto.comment.request.CreateSubCommentRequest;
import com.x.backend.dto.comment.request.EditSubCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.utils.api.BaseApiResponse;

/**
 * Service for managing subcomments (replies) under comments.
 */
public interface PostSubCommentService {

    /**
     * Creates a subcomment (reply) under a comment.
     *
     * @param username Author of the reply
     * @param postId Post ID
     * @param req Request containing content and parent comment ID
     * @return Created subcomment response
     */
    BaseApiResponse<CommentResponse> createSubComment(String username, Long postId, CreateSubCommentRequest req);

    /**
     * Edits a subcomment.
     *
     * @param username User requesting edit
     * @param subCommentId ID of the subcomment
     * @param req New content
     * @return Edited subcomment response
     */
    BaseApiResponse<CommentResponse> editSubComment(String username, Long subCommentId, EditSubCommentRequest req);

    /**
     * Soft-deletes a subcomment.
     *
     * @param username User requesting deletion
     * @param subCommentId Subcomment ID
     * @return Confirmation response
     */
    BaseApiResponse<String> deleteSubComment(String username, Long subCommentId);

}
