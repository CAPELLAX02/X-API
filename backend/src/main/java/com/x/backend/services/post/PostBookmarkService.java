package com.x.backend.services.post;

import com.x.backend.exceptions.post.PostAlreadyBookmarkedException;
import com.x.backend.exceptions.post.PostHaveNotBookmarkedException;
import com.x.backend.exceptions.post.PostBaseNotFoundException;
import com.x.backend.utils.api.BaseApiResponse;

/**
 * Service interface for managing bookmarking functionality for posts.
 * <p>
 * This includes adding or removing a bookmark, checking bookmark status,
 * and retrieving bookmark counts.
 * </p>
 */
public interface PostBookmarkService {

    /**
     * Adds a bookmark to the specified post for the given user.
     *
     * @param username the username of the user bookmarking the post
     * @param postId   the ID of the post to be bookmarked
     * @return success message if bookmarked successfully
     * @throws PostAlreadyBookmarkedException if the user has already bookmarked the post
     * @throws PostBaseNotFoundException if the post with the given ID does not exist
     */
    BaseApiResponse<String> bookmarkPost(String username, Long postId);

    /**
     * Removes an existing bookmark from the specified post.
     *
     * @param username the username of the user removing the bookmark
     * @param postId   the ID of the post to remove the bookmark from
     * @return success message if bookmark is removed
     * @throws PostHaveNotBookmarkedException if the user has not bookmarked the post
     * @throws PostBaseNotFoundException if the post with the given ID does not exist
     */
    BaseApiResponse<String> removeBookmark(String username, Long postId);

    /**
     * Checks whether the specified user has bookmarked the given post.
     *
     * @param username the username of the user
     * @param postId   the ID of the post
     * @return {@code true} if the post is bookmarked by the user, otherwise {@code false}
     * @throws PostBaseNotFoundException if the post with the given ID does not exist
     */
    BaseApiResponse<Boolean> hasUserBookmarked(String username, Long postId);

    /**
     * Returns the number of bookmarks for the given post.
     *
     * @param postId the ID of the post
     * @return total bookmark count
     * @throws PostBaseNotFoundException if the post with the given ID does not exist
     */
    BaseApiResponse<Long> getBookmarkCount(Long postId);

}
