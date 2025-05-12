package com.x.backend.services.comment;

import com.x.backend.dto.comment.request.CreateCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.exceptions.post.CommentNotFoundException;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

/**
 * Service interface for managing comments on posts.
 * <p>
 * This service handles creation of new comments (including nested replies),
 * and retrieval of comment threads belonging to a specific post.
 * </p>
 */
public interface CommentService {

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
