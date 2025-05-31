package com.x.backend.services.post;

import com.x.backend.dto.post.request.CreatePostRequest;
import com.x.backend.dto.post.response.PostResponse;
import com.x.backend.exceptions.image.MaxImageLimitExceededException;
import com.x.backend.exceptions.post.PostBaseNotFoundException;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface responsible for managing operations related to user-generated posts.
 * <p>
 * This includes creation of posts (with optional polls and media attachments), retrieval of posts with
 * respect to audience visibility, author-specific post listings, and generating a user's timeline.
 * </p>
 */
public interface PostService {

    /**
     * Creates a new post authored by the given user, optionally attaching media files and/or a poll.
     *
     * @param username     the username of the post author
     * @param req          the post creation request including:
     *                     - {@code content}: textual content of the post
     *                     - {@code audience}: visibility level (e.g., public, followers only)
     *                     - {@code replyRestriction}: who can reply
     *                     - {@code replyToPostId}: (optional) decides if the post is a reply post to another post
     *                     - {@code pollOptions} (optional): list of poll choices if a poll is included
     *                     - {@code pollExpiryDate} (optional): when the poll expires
     *                     - {@code scheduledDate} (optional): for scheduling future posts
     * @param postImages   optional list of images to attach to the post (max 5 allowed)
     * @return a response wrapping the created post and a success message
     * @throws MaxImageLimitExceededException if more than 5 images are uploaded
     */
    BaseApiResponse<PostResponse> createPost(
            String username,
            CreatePostRequest req,
            List<MultipartFile> postImages
    );

    /**
     * Retrieves a post by its ID, while enforcing audience-based access control logic.
     * <ul>
     *     <li>If the post is private, only the author can view it.</li>
     *     <li>If it's restricted to followers or mentioned users, appropriate checks are enforced.</li>
     * </ul>
     *
     * @param postId          the unique ID of the post
     * @param currentUsername the currently authenticated user's username (nullable for guests)
     * @return the post if access is allowed, otherwise throws {@code AccessDeniedException}
     * @throws PostBaseNotFoundException if the post does not exist
     * @throws AccessDeniedException if access is not permitted
     */
    BaseApiResponse<PostResponse> getPostByIdWithAccessControl(Long postId, String currentUsername);

    /**
     * Retrieves all posts authored by the specified user, ordered by creation date descending.
     *
     * @param author the username of the author whose posts are to be retrieved
     * @return a list of posts authored by the user
     */
    BaseApiResponse<List<PostResponse>> getPostsByAuthor(String author);

    /**
     * Constructs a timeline of posts for the specified user, consisting of:
     * <ul>
     *     <li>Posts by users they follow</li>
     *     <li>Their own posts</li>
     * </ul>
     *
     * @TODO: This method is going to be optimized not to see an N+1 problem!
     *
     * @param currentUsername the username of the user whose timeline is being fetched
     * @return a list of posts forming the user's timeline
     */
    BaseApiResponse<List<PostResponse>> getTimeline(String currentUsername);

    /**
     * Counts the number of posts authored by a given user.
     *
     * @param username the username of the user whose post count is requested
     * @return the total number of posts created by the user
     */
    BaseApiResponse<Long> getUserPostCount(String username);

}
