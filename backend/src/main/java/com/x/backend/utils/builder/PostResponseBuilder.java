package com.x.backend.utils.builder;

import com.x.backend.dto.post.response.PollOptionResponse;
import com.x.backend.dto.post.response.PollResponse;
import com.x.backend.dto.post.response.PostResponse;
import com.x.backend.models.entities.*;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.auth.AuthUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostResponseBuilder {

    private final UserService userService;
    private final AuthUtils authUtils;

    public PostResponseBuilder(UserService userService, AuthUtils authUtils) {
        this.userService = userService;
        this.authUtils = authUtils;
    }

    public PostResponse buildPostResponse(Post p) {
        String currentUsername = authUtils.getAuthenticatedUsername();
        ApplicationUser currentUser = userService.getUserByUsername(currentUsername);

        return new PostResponse(
                p.getId(),
                p.getAuthor().getUsername(),
                p.getAuthor().getNickname(),
                Optional.ofNullable(p.getAuthor().getProfilePicture()).map(Image::getImageUrl).orElse(null),
                p.getContent(),
                p.getCreatedAt(),
                p.isReply(),
                Optional.ofNullable(p.getReplyTo()).map(Post::getId).orElse(null),
                p.getLikes().size(),
                p.getComments().size(),
                p.getReposts().size(),
                p.getBookmarks().size(),
                p.getViews().size(),
                p.getLikes().stream().anyMatch(like -> like.getUser().equals(currentUser)),
                p.getBookmarks().contains(currentUser),
                p.getMediaAttachments().stream().map(Image::getImageUrl).toList(),
                p.getAudience(),
                p.getReplyRestriction(),
                buildPollResponse(p.getPoll(), currentUser)
        );
    }

    private PollResponse buildPollResponse(Poll poll, ApplicationUser currentUser) {
        if (poll == null) return null;

        List<PollVote> allVotes = poll.getVotes();
        boolean hasVoted = allVotes.stream().anyMatch(v -> v.getUser().equals(currentUser));

        PollVote userVote = allVotes.stream()
                .filter(v -> v.getUser().equals(currentUser))
                .findFirst()
                .orElse(null);

        Integer selectedOptionIndex = null;
        if (userVote != null) {
            PollOption selectedOption = userVote.getOption();
            selectedOptionIndex = poll.getOptions().indexOf(selectedOption);
        }

        List<PollOptionResponse> optionResponses = poll.getOptions().stream()
                .map(option -> {
                    int voteCount = (int) allVotes.stream()
                            .filter(vote -> vote.getOption().equals(option))
                            .count();
                    int index = poll.getOptions().indexOf(option);
                    return new PollOptionResponse(index, option.getOptionText(), voteCount);
                })
                .toList();

        return new PollResponse(
                poll.getId(),
                optionResponses,
                poll.getExpiresAt(),
                hasVoted,
                selectedOptionIndex
        );
    }


}
