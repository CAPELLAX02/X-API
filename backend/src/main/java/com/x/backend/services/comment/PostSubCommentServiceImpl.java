package com.x.backend.services.comment;

import com.x.backend.dto.comment.request.CreateSubCommentRequest;
import com.x.backend.dto.comment.request.EditSubCommentRequest;
import com.x.backend.dto.comment.response.SubCommentResponse;
import com.x.backend.exceptions.post.CommentBaseNotFoundException;
import com.x.backend.exceptions.post.PostBaseNotFoundException;
import com.x.backend.models.post.Post;
import com.x.backend.models.post.comment.Comment;
import com.x.backend.models.post.comment.SubComment;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.repositories.CommentRepository;
import com.x.backend.repositories.PostRepository;
import com.x.backend.repositories.SubCommentRepository;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.builder.CommentResponseBuilder;
import com.x.backend.utils.builder.SubCommentResponseBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostSubCommentServiceImpl implements PostSubCommentService {

    private final SubCommentRepository subCommentRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentResponseBuilder commentResponseBuilder;
    private final SubCommentResponseBuilder subCommentResponseBuilder;

    public PostSubCommentServiceImpl(final SubCommentRepository subCommentRepository,
                                     final CommentRepository commentRepository,
                                     final PostRepository postRepository,
                                     final UserService userService,
                                     final CommentResponseBuilder commentResponseBuilder,
                                     final SubCommentResponseBuilder subCommentResponseBuilder
    ) {
        this.subCommentRepository = subCommentRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentResponseBuilder = commentResponseBuilder;
        this.subCommentResponseBuilder = subCommentResponseBuilder;
    }

    @Override
    public BaseApiResponse<SubCommentResponse> createSubComment(String username, Long postId, Long parentCommentId, CreateSubCommentRequest req) {
        ApplicationUser subCommentAuthor = userService.getUserByUsername(username);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostBaseNotFoundException(postId));
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() -> new CommentBaseNotFoundException(parentCommentId));

        validateReplyPermission(subCommentAuthor, post);

        SubComment subComment = new SubComment();
        subComment.setAuthor(subCommentAuthor);
        subComment.setPost(post);
        subComment.setParentComment(parentComment);
        subComment.setContent(req.content());
        subComment.setCreatedAt(LocalDateTime.now());

        SubComment savedSubComment = subCommentRepository.save(subComment);

        SubCommentResponse subCommentResponse = subCommentResponseBuilder.build(savedSubComment);
        return BaseApiResponse.success(subCommentResponse, "Subcomment created successfully");
    }

    @Override
    public BaseApiResponse<SubCommentResponse> editSubComment(String username, Long subCommentId, EditSubCommentRequest req) {
        ApplicationUser subCommentAuthor = userService.getUserByUsername(username);
        SubComment subComment = subCommentRepository.findById(subCommentId).orElseThrow(() -> new CommentBaseNotFoundException(subCommentId));

        if (!subComment.getAuthor().getId().equals(subCommentAuthor.getId())) {
            throw new AccessDeniedException("You do not have permission to edit this subcomment");
        }

        subComment.setContent(req.newContent());
        subComment.setEditedAt(LocalDateTime.now());

        subCommentRepository.save(subComment);

        SubCommentResponse subCommentResponse = subCommentResponseBuilder.build(subComment);
        return BaseApiResponse.success(subCommentResponse, "Subcomment edited successfully");
    }

    @Override
    public BaseApiResponse<String> deleteSubComment(String username, Long subCommentId) {
        ApplicationUser subCommentAuthor = userService.getUserByUsername(username);
        SubComment subComment = subCommentRepository.findById(subCommentId).orElseThrow(() -> new CommentBaseNotFoundException(subCommentId));

        if (!subComment.getAuthor().getId().equals(subCommentAuthor.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this subcomment");
        }

        subComment.setDeleted(true);
        subComment.setEditedAt(LocalDateTime.now());

        subCommentRepository.save(subComment);
        return BaseApiResponse.success("Subcomment deleted successfully");
    }

    private void validateReplyPermission(ApplicationUser currentUser, Post post) {
        switch (post.getReplyRestriction()) {
            case NO_ONE -> throw new AccessDeniedException("Replies are disabled for this post");
            case FOLLOWERS_ONLY -> {
                if (!post.getAuthor().getFollowers().contains(currentUser)) {
                    throw new AccessDeniedException("Only followers can reply");
                }
            }
            case MENTIONED_ONLY -> {
                if (post.getContent() == null || !post.getContent().contains("@" + currentUser.getNickname())) {
                    throw new AccessDeniedException("Only mentioned users can reply");
                }
            }
            case EVERYONE -> {} // allowed
            default -> throw new AccessDeniedException("Unsupported restriction");
        }
    }

}
