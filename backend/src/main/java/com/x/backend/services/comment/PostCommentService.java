package com.x.backend.services.comment;

import com.x.backend.dto.comment.request.CreateCommentRequest;
import com.x.backend.dto.comment.request.EditCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.exceptions.post.CommentNotFoundException;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

/**
 * Service interface for managing comments on posts.
 * <p>
 * This service handles creation of new comments (including nested replies),
 * and retrieval of comment threads belonging to a specific post.
 * </p>
 */
public interface PostCommentService {

    /**
     * Creates a new comment for a given post, optionally as a reply to another comment.
     * <p>
     * Applies reply restriction logic based on post settings (e.g. followers-only, mentioned-only).
     * </p>
     *
     * @param username the username of the currently authenticated user creating the comment
     * @param postId   the ID of the post to which the comment is being added
     * @param req      the request body containing:
     *                 <ul>
     *                   <li>{@code content} — the text of the comment</li>
     *                   <li>{@code parentCommentId} (optional) — ID of the parent comment if this is a reply</li>
     *                 </ul>
     * @return the created comment in response DTO format
     *
     * @throws PostNotFoundException if the post with the given ID does not exist
     * @throws CommentNotFoundException if the specified parent comment does not exist
     * @throws AccessDeniedException if the user is not allowed to reply due to post restrictions
     */
    BaseApiResponse<CommentResponse> createComment(String username, Long postId, CreateCommentRequest req);

    /**
     * Edits the content of a comment created by the authenticated user.
     *
     * @param username the username of the currently authenticated user attempting the edit
     * @param commentId the ID of the comment to be edited
     * @param req contains the new content to replace the old comment text
     * @return a response containing the updated comment
     *
     * @throws CommentNotFoundException if the comment with given ID does not exist
     * @throws AccessDeniedException if the comment does not belong to the current user
     * @throws IllegalArgumentException if the new content is invalid (e.g., blank or too long)
     */
    BaseApiResponse<CommentResponse> editComment(String username, Long commentId, EditCommentRequest req);

    /**
     * Soft deletes the comment by marking it as deleted.
     * Only the original author of the comment is allowed to perform this action.
     *
     * @param username the username of the user attempting the deletion
     * @param commentId the ID of the comment to delete
     * @return a success message wrapped in a BaseApiResponse
     *
     * @throws CommentNotFoundException if the comment is not found by ID
     * @throws AccessDeniedException if the user is not the author of the comment
     */
    BaseApiResponse<String> deleteComment(String username, Long commentId);

    /**
     * Retrieves all comments associated with a given post, structured into a hierarchical format.
     * <p>
     * Top-level comments are returned with their immediate child replies grouped inside.
     * </p>
     *
     * @param postId the ID of the post whose comments should be fetched
     * @return a list of top-level comments and their grouped replies
     *
     * @throws PostNotFoundException if the post with the given ID does not exist
     */
    BaseApiResponse<List<CommentResponse>> getCommentsByPost(Long postId);

}
