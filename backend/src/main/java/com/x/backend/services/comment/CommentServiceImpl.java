package com.x.backend.services.comment;

import com.x.backend.dto.comment.request.CreateCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.exceptions.post.CommentNotFoundException;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.models.entities.Comment;
import com.x.backend.models.entities.Post;
import com.x.backend.repositories.CommentRepository;
import com.x.backend.repositories.PostRepository;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.builder.CommentResponseBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.Port;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentResponseBuilder commentResponseBuilder;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              UserService userService,
                              CommentResponseBuilder commentResponseBuilder)
    {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentResponseBuilder = commentResponseBuilder;
    }

    @Override
    public BaseApiResponse<CommentResponse> createComment(String username, Long postId, CreateCommentRequest req) {
        ApplicationUser commentAuthor = userService.getUserByUsername(username);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        validateReplyPermission(commentAuthor, post);

        Comment comment = new Comment();
        comment.setAuthor(commentAuthor);
        comment.setPost(post);
        comment.setContent(req.content());
        comment.setCreatedAt(LocalDateTime.now());

        if (req.parentCommentId() != null) {
            Comment parentComment = commentRepository.findById(req.parentCommentId())
                    .orElseThrow(() ->  new CommentNotFoundException(req.parentCommentId()));
            comment.setParentComment(parentComment);
        }

        Comment savedComment = commentRepository.save(comment);
        CommentResponse commentResponse = commentResponseBuilder.buildCommentResponse(savedComment);

        return BaseApiResponse.success(commentResponse, "Comment created successfully");
    }

    @Override
    public BaseApiResponse<List<CommentResponse>> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        List<Comment> allComments = commentRepository.findAllByPostOrderByCreatedAtDesc(post);

        // Group parent comment
        Map<Long, List<Comment>> groupedComments = allComments.stream()
                .filter(comment -> comment.getParentComment() != null)
                .collect(Collectors.groupingBy(comment -> comment.getParentComment().getId()));

        List<CommentResponse> topLevelComments = allComments.stream()
                .filter(comment -> comment.getParentComment() == null)
                .map(comment -> commentResponseBuilder.buildCommentResponse(comment, groupedComments))
                .toList();

        return BaseApiResponse.success(topLevelComments, "Comments retrieved successfully");
    }

    private boolean isUserMentionedInPost(ApplicationUser user, Post post) {
        String content = post.getContent();
        String nickname = user.getNickname();
        return content != null && content.contains("@" + nickname);
    }

    private void validateReplyPermission(ApplicationUser currentUser, Post post) {
        switch (post.getReplyRestriction()) {
            case NO_ONE -> {
                throw new AccessDeniedException("Replies are disabled for this post");
            }
            case FOLLOWERS_ONLY -> {
                ApplicationUser author = post.getAuthor();
                if (!author.getFollowers().contains(currentUser)) {
                    throw new AccessDeniedException("Only followers can reply to this post");
                }
            }
            case MENTIONED_ONLY -> {
                if (!isUserMentionedInPost(currentUser, post)) {
                    throw new AccessDeniedException("Only mentioned users can reply to this post");
                }
            }
            case EVERYONE -> {
                // No restriction
            }
            default -> {
                throw new AccessDeniedException("Unsupported reply restriction: " + post.getReplyRestriction());
            }
        }
    }

}
