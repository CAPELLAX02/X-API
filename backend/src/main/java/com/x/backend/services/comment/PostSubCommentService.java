package com.x.backend.services.comment;

import com.x.backend.dto.comment.request.CreateSubCommentRequest;
import com.x.backend.dto.comment.request.EditSubCommentRequest;
import com.x.backend.dto.comment.response.SubCommentResponse;
import com.x.backend.exceptions.post.CommentBaseNotFoundException;
import com.x.backend.exceptions.post.PostBaseNotFoundException;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.security.access.AccessDeniedException;

/**
 * Service interface for managing SubComments (replies to comments).
 * <p>
 * SubComments are second-level responses that are directly tied to a parent {@link com.x.backend.models.post.comment.Comment},
 * but still indirectly related to a {@link com.x.backend.models.post.Post}.
 * This model avoids nested recursion by restricting replies to only one level of depth.
 * <p>
 * By separating SubComments from Comments:
 * <ul>
 *     <li>UI rendering logic becomes cleaner (single parent per subcomment)</li>
 *     <li>Database constraints and queries become easier to maintain</li>
 *     <li>Future pagination, moderation, or analysis features are easier to implement</li>
 * </ul>
 */
public interface PostSubCommentService {

    /**
     * Creates a new subcomment under a given parent comment.
     *
     * @param username   the username of the subcomment author
     * @param postId     the post to which the parent comment belongs
     * @param commentId  the parent comment ID
     * @param req        the request payload containing content
     * @return a response containing the created subcomment
     * @throws CommentBaseNotFoundException if the parent comment does not exist
     * @throws PostBaseNotFoundException if the post does not exist
     * @throws AccessDeniedException if the user is not allowed to reply
     */
    BaseApiResponse<SubCommentResponse> createSubComment(String username, Long postId, Long commentId, CreateSubCommentRequest req);

    /**
     * Edits an existing subcomment.
     *
     * @param username      the user attempting the edit
     * @param subCommentId  the ID of the subcomment to be edited
     * @param req           the request payload containing new content
     * @return a response containing the updated subcomment
     * @throws CommentBaseNotFoundException if the subcomment does not exist
     * @throws AccessDeniedException if the user is not the author
     */
    BaseApiResponse<SubCommentResponse> editSubComment(String username, Long subCommentId, EditSubCommentRequest req);

    /**
     * Soft-deletes a subcomment.
     *
     * @param username      the user attempting the deletion
     * @param subCommentId  the ID of the subcomment to be deleted
     * @return a confirmation response
     * @throws CommentBaseNotFoundException if the subcomment does not exist
     * @throws AccessDeniedException if the user is not the author
     */
    BaseApiResponse<String> deleteSubComment(String username, Long subCommentId);
}

