package com.x.backend.services.comment;

import com.x.backend.dto.comment.request.CreateCommentRequest;
import com.x.backend.dto.comment.request.EditCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.exceptions.post.CommentNotFoundException;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.models.post.Post;
import com.x.backend.models.post.comment.Comment;
import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.repositories.CommentRepository;
import com.x.backend.repositories.PostRepository;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.builder.CommentResponseBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentResponseBuilder commentResponseBuilder;

    public PostCommentServiceImpl(CommentRepository commentRepository,
                                  PostRepository postRepository,
                                  UserService userService,
                                  CommentResponseBuilder commentResponseBuilder
    ) {
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

        Comment savedComment = commentRepository.save(comment);
        CommentResponse commentResponse = commentResponseBuilder.build(savedComment);

        return BaseApiResponse.success(commentResponse, "Comment created successfully");
    }

    @Override
    public BaseApiResponse<CommentResponse> editComment(String username, Long commentId, EditCommentRequest req) {
        ApplicationUser commentAuthor = userService.getUserByUsername(username);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!comment.getAuthor().getId().equals(commentAuthor.getId())) {
            throw new AccessDeniedException("You do not have permission to edit this comment");
        }

        comment.setContent(req.newContent());
        comment.setEditedAt(LocalDateTime.now());

        Comment editedComment = commentRepository.save(comment);
        CommentResponse commentResponse = commentResponseBuilder.build(editedComment);

        return BaseApiResponse.success(commentResponse, "Comment edited successfully");
    }

    @Override
    public BaseApiResponse<String> deleteComment(String username, Long commentId) {
        ApplicationUser commentAuthor = userService.getUserByUsername(username);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!comment.getAuthor().getId().equals(commentAuthor.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this comment");
        }

        comment.setDeleted(true);
        comment.setEditedAt(LocalDateTime.now());

        commentRepository.save(comment);

        return BaseApiResponse.success("Comment deleted successfully");
    }

    @Override
    public BaseApiResponse<List<CommentResponse>> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        List<Comment> comments = commentRepository.findAllByPostOrderByCreatedAtDesc(post);

        List<CommentResponse> commentResponses = comments.stream()
                .map(commentResponseBuilder::build)
                .toList();

        return BaseApiResponse.success(commentResponses, "Comments retrieved successfully");
    }

    private void validateReplyPermission(ApplicationUser currentUser, Post post) {
        switch (post.getReplyRestriction()) {
            case NO_ONE -> throw new AccessDeniedException("Replies are disabled for this post");
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
            default -> throw new AccessDeniedException("Unsupported reply restriction: " + post.getReplyRestriction());
        }
    }

    private boolean isUserMentionedInPost(ApplicationUser user, Post post) {
        String content = post.getContent();
        String nickname = user.getNickname();
        return content != null && content.contains("@" + nickname);
    }

}
