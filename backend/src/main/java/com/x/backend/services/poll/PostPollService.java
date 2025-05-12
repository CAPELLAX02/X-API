package com.x.backend.services.poll;

import com.x.backend.dto.poll.request.PollVoteRequest;
import com.x.backend.exceptions.poll.*;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.utils.api.BaseApiResponse;

/**
 * Service interface for managing user interaction with polls associated with posts.
 * <p>
 * This service handles vote submission and revocation for poll-type posts.
 * It enforces all related business rules such as expiration checks, duplicate votes,
 * and valid option indices.
 * </p>
 */
public interface PostPollService {

    /**
     * Submits a user's vote to a poll attached to a specific post.
     * <p>
     * Validates the poll existence, expiry status, and ensures that the user has not already voted.
     * Also ensures that the selected option index is within valid bounds.
     * </p>
     *
     * @param username the username of the authenticated user submitting the vote
     * @param postId   the ID of the post that contains the poll
     * @param req      the request body containing the {@code selectedOptionIndex}
     * @return success message upon vote submission
     *
     * @throws PostNotFoundException           if the post with the given ID does not exist
     * @throws PostDoesNotHaveAPollException   if the post has no associated poll
     * @throws PollHasExpiredException         if the poll has already expired
     * @throws PollAlreadyVotedInException     if the user has already voted in the poll
     * @throws InvalidPollOptionIndexException if the provided option index is out of range
     */
    BaseApiResponse<String> voteInPoll(String username, Long postId, PollVoteRequest req);

    /**
     * Revokes a user's previously submitted vote from a poll.
     * <p>
     * This is a custom extension beyond standard Twitter behavior.
     * Vote revocation is allowed only if the poll is still active.
     * </p>
     *
     * @param username the username of the user revoking the vote
     * @param pollId   the ID of the poll from which to remove the user's vote
     * @return success message upon successful revocation
     *
     * @throws PollNotFoundException       if the poll with the given ID does not exist
     * @throws PollHasExpiredException     if the poll has already expired
     * @throws PollVoteNotFoundException   if no vote by this user exists in the poll
     */
    BaseApiResponse<String> revokePollVote(String username, Long pollId);

}
