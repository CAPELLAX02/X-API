package com.x.backend.services.post;

import com.x.backend.exceptions.post.PostAlreadyRepostedException;
import com.x.backend.exceptions.post.PostHaveNotRepostedException;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.utils.api.BaseApiResponse;

/**
 * Service interface that manages reposting functionality for posts.
 * <p>
 * Users can repost posts, undo reposts, check if they have reposted a post,
 * and retrieve the total repost count for any given post.
 * </p>
 */
public interface PostRepostService {

    /**
     * Reposts the specified post on behalf of the authenticated user.
     *
     * @param username the username of the user performing the repost
     * @param postId   the ID of the post to repost
     * @return success message if repost is added successfully
     * @throws PostAlreadyRepostedException if the user has already reposted the post
     * @throws PostNotFoundException if the post with the given ID does not exist
     */
    BaseApiResponse<String> repostPost(String username, Long postId);

    /**
     * Undoes a previously made repost by the user.
     *
     * @param username the username of the user removing the repost
     * @param postId   the ID of the post to undo the repost for
     * @return success message if the repost is successfully removed
     * @throws PostHaveNotRepostedException if the user has not reposted the post
     * @throws PostNotFoundException if the post with the given ID does not exist
     */
    BaseApiResponse<String> undoRepost(String username, Long postId);

    /**
     * Retrieves the total number of reposts for a given post.
     *
     * @param postId the ID of the post
     * @return the count of reposts for the post
     * @throws PostNotFoundException if the post with the given ID does not exist
     */
    BaseApiResponse<Long> getRepostCount(Long postId);

    /**
     * Checks whether the specified user has reposted the given post.
     *
     * @param username the username of the user
     * @param postId   the ID of the post
     * @return {@code true} if the user has reposted the post, {@code false} otherwise
     * @throws PostNotFoundException if the post with the given ID does not exist
     */
    BaseApiResponse<Boolean> hasUserReposted(String username, Long postId);

}
