package com.x.backend.services.comment;

import com.x.backend.dto.comment.request.CreateCommentRequest;
import com.x.backend.dto.comment.request.EditCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.exceptions.post.CommentBaseNotFoundException;
import com.x.backend.exceptions.post.PostBaseNotFoundException;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

/**
 * Service interface for managing top-level comments in the system.
 * <p>
 * In this architecture, comments are split into two separate hierarchies:
 * <ul>
 *     <li><b>Comment</b>: Represents top-level user responses directly associated with a post.</li>
 *     <li><b>SubComment</b>: Represents user replies to a specific comment, not directly to the post.</li>
 * </ul>
 * This separation ensures:
 * <ul>
 *     <li>Clear relational boundaries</li>
 *     <li>Better query performance on both levels</li>
 *     <li>Simplified moderation and UI rendering logic</li>
 * </ul>
 *
 * All methods assume that authentication and authorization are handled via Spring Security,
 * and that user-related validations are performed through injected dependencies.
 */
public interface PostCommentService {

    /**
     * Creates a new top-level comment on the given post.
     *
     * @param username the username of the currently authenticated user
     * @param postId   the ID of the post to comment on
     * @param req      the request body containing the content of the comment
     * @return a response containing the created comment
     * @throws PostBaseNotFoundException if the target post is not found
     * @throws AccessDeniedException if the user is not allowed to comment
     */
    BaseApiResponse<CommentResponse> createComment(String username, Long postId, CreateCommentRequest req);

    /**
     * Edits the content of an existing comment.
     *
     * @param username  the user attempting the edit
     * @param commentId the ID of the comment to be edited
     * @param req       the request body containing new content
     * @return a response containing the updated comment
     * @throws CommentBaseNotFoundException if the comment does not exist
     * @throws AccessDeniedException if the user is not the comment author
     */
    BaseApiResponse<CommentResponse> editComment(String username, Long commentId, EditCommentRequest req);

    /**
     * Soft-deletes a comment by marking it as deleted.
     *
     * @param username  the user attempting to delete
     * @param commentId the ID of the comment to be deleted
     * @return a confirmation response
     * @throws CommentBaseNotFoundException if the comment does not exist
     * @throws AccessDeniedException if the user is not the comment author
     */
    BaseApiResponse<String> deleteComment(String username, Long commentId);

    /**
     * Retrieves all top-level comments associated with a specific post.
     *
     * @param postId the ID of the post whose comments are to be fetched
     * @return a list of comments sorted by creation time (descending)
     * @throws PostBaseNotFoundException if the post does not exist
     */
    BaseApiResponse<List<CommentResponse>> getCommentsByPost(Long postId);

}
