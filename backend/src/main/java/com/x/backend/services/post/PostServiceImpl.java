package com.x.backend.services.post;

import com.x.backend.dto.post.request.CreatePostRequest;
import com.x.backend.dto.post.request.PostInteractionContext;
import com.x.backend.dto.post.response.PostResponse;
import com.x.backend.exceptions.image.MaxImageLimitExceededException;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.models.media.Image;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.post.poll.Poll;
import com.x.backend.models.post.poll.PollOption;
import com.x.backend.models.post.poll.PollVote;
import com.x.backend.models.post.Post;
import com.x.backend.repositories.PollOptionRepository;
import com.x.backend.repositories.PollRepository;
import com.x.backend.repositories.PostRepository;
import com.x.backend.services.media.image.PostImageService;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.builder.PostResponseBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PollOptionRepository pollOptionRepository;
    private final PollRepository pollRepository;
    private final UserService userService;
    private final PostResponseBuilder postResponseBuilder;
    private final PostImageService postImageService;

    public PostServiceImpl(final PostRepository postRepository,
                           final PollOptionRepository pollOptionRepository,
                           final PollRepository pollRepository,
                           final UserService userService,
                           final PostResponseBuilder postResponseBuilder,
                           final PostImageService postImageService
    ) {
        this.postRepository = postRepository;
        this.pollOptionRepository = pollOptionRepository;
        this.pollRepository = pollRepository;
        this.userService = userService;
        this.postResponseBuilder = postResponseBuilder;
        this.postImageService = postImageService;
    }

    private PostInteractionContext buildPostInteractionContext(Post post, ApplicationUser user) {
        boolean liked = post.getLikes().stream().anyMatch(like -> like.getUser().equals(user));
        boolean bookmarked = post.getBookmarks().contains(user);
        boolean hasVoted = false;
        int selectedIndex = -1;

        if (post.getPoll() != null) {
            List<PollVote> votes = post.getPoll().getVotes();
            hasVoted = votes.stream().anyMatch(vote -> vote.getUser().equals(user));
            selectedIndex = votes.stream()
                    .filter(vote -> vote.getUser().equals(user))
                    .findFirst()
                    .map(vote -> post.getPoll().getOptions().indexOf(vote.getOption()))
                    .orElse(-1);
        }
        return new PostInteractionContext(liked, bookmarked, hasVoted, selectedIndex);
    }

    @Override
    public BaseApiResponse<PostResponse> createPost(
            String username,
            CreatePostRequest req,
            List<MultipartFile> postImages
    ) {
        ApplicationUser author = userService.getUserByUsername(username);

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(req.content());
        post.setAudience(req.audience());
        post.setReplyRestriction(req.replyRestriction());
        post.setScheduledDate(req.scheduledDate());

        if (req.replyToPostId() != null) {
            Post parentPost = postRepository.findById(req.replyToPostId()).orElseThrow(() -> new PostNotFoundException(req.replyToPostId()));

            validatePostViewPermission(author, parentPost);

            post.setReplyTo(parentPost);
            post.setReply(true);
        }

        if (postImages != null && !postImages.isEmpty()) {
            List<Image> uploadedImages = postImageService.uploadPostImages(postImages);
            post.setMediaAttachments(uploadedImages);
        }

        if (postImages != null && postImages.size() > 5) {
            throw new MaxImageLimitExceededException(5);
        }

        Post savedPost = postRepository.save(post);

        if (req.poll() != null) {
            var pollReq = req.poll();

            Poll poll = new Poll();
            poll.setPost(post);
            poll.setExpiresAt(pollReq.pollExpiryDate());

            List<PollOption> options = new ArrayList<>();
            for (String optionText : pollReq.pollOptions()) {
                PollOption option = new PollOption();
                option.setPoll(poll);
                option.setOptionText(optionText);
                options.add(option);
            }

            poll.setOptions(options);
            post.setPoll(poll);

            pollRepository.save(poll);
            pollOptionRepository.saveAll(options);
        }

        PostResponse postResponse = postResponseBuilder.build(savedPost, buildPostInteractionContext(savedPost, author));
        return BaseApiResponse.success(postResponse, "Post created successfully.");
    }

    private boolean isUserMentionedInPost(ApplicationUser user, Post post) {
        String content = post.getContent();
        String nickname = user.getNickname();
        return content != null && content.contains("@" + nickname);
    }

    private void validatePostViewPermission(ApplicationUser viewer, Post post) {
        switch (post.getAudience()) {
            case PRIVATE -> {
                if (viewer == null || !post.getAuthor().getId().equals(viewer.getId())) {
                    throw new AccessDeniedException("This post is private.");
                }
            }
            case FOLLOWERS_ONLY -> {
                if (viewer == null || !post.getAuthor().getFollowers().contains(viewer)) {
                    throw new AccessDeniedException("Only followers can view this post.");
                }
            }
            case MENTIONED_ONLY -> {
                if (viewer == null || !isUserMentionedInPost(viewer, post)) {
                    throw new AccessDeniedException("Only mentioned users can view this post.");
                }
            }
            case EVERYONE -> { /* No restriction */ }
            default -> throw new AccessDeniedException("Unsupported audience: " + post.getAudience());
        }
    }

    @Override
    public BaseApiResponse<PostResponse> getPostByIdWithAccessControl(Long postId, String currentUsername) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        ApplicationUser viewer = (currentUsername != null)
                ? userService.getUserByUsername(currentUsername)
                : null;

        validatePostViewPermission(viewer, post);

        PostInteractionContext ctx = (viewer != null)
                ? buildPostInteractionContext(post, viewer)
                : new PostInteractionContext(false, false, false, -1); // anonymous user

        PostResponse postResponse = postResponseBuilder.build(post, ctx);
        return BaseApiResponse.success(postResponse, "Post retrieved successfully.");
    }

    @Override
    public BaseApiResponse<List<PostResponse>> getPostsByAuthor(String author) {
        ApplicationUser authorUser = userService.getUserByUsername(author);
        List<Post> posts = postRepository.findAllByAuthorOrderByCreatedAtDesc(authorUser);
        List<PostResponse> postResponseList = posts.stream()
                .map(p -> postResponseBuilder.build(p, buildPostInteractionContext(p, authorUser)))
                .toList();

        return BaseApiResponse.success(postResponseList, "Posts of author (" + author + ") retrieved successfully.");
    }

    @Override
    public BaseApiResponse<List<PostResponse>> getTimeline(String currentUsername) {
        ApplicationUser user = userService.getUserByUsername(currentUsername);
        List<ApplicationUser> followedUsers = new ArrayList<>(user.getFollowing());
        followedUsers.add(user);

        List<Post> timelinePosts = postRepository.findAllByAuthorInOrderByCreatedAtDesc(followedUsers);
        List<PostResponse> responseList = timelinePosts.stream()
                .map(p -> postResponseBuilder.build(p, buildPostInteractionContext(p, user)))
                .toList();

        return BaseApiResponse.success(responseList, "Post timeline retrieved successfully.");
    }

    @Override
    public BaseApiResponse<Long> getUserPostCount(String currentUsername) {
        ApplicationUser user = userService.getUserByUsername(currentUsername);
        long count = postRepository.countByAuthorId(user.getId());
        return BaseApiResponse.success(count, "Post count retrieved successfully.");
    }

}
