package com.x.backend.services.like;

import com.x.backend.exceptions.post.CommentAlreadyLikedException;
import com.x.backend.exceptions.post.CommentHaveNotLikedException;
import com.x.backend.exceptions.post.CommentNotFoundException;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.models.entities.Comment;
import com.x.backend.repositories.CommentRepository;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentLikeServiceImpl(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Override
    public BaseApiResponse<String> likeComment(String authenticatedUsername, Long commentId) {
        ApplicationUser authenticatedUser = userService.getUserByUsername(authenticatedUsername);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));

        if (comment.getLikes().contains(authenticatedUser)) throw new CommentAlreadyLikedException();

        comment.getLikes().add(authenticatedUser);
        commentRepository.save(comment);

        return BaseApiResponse.success("Comment liked");
    }

    @Override
    public BaseApiResponse<String> unlikeComment(String authenticatedUsername, Long commentId) {
        ApplicationUser authenticatedUser = userService.getUserByUsername(authenticatedUsername);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!comment.getLikes().contains(authenticatedUser)) throw new CommentHaveNotLikedException();

        comment.getLikes().remove(authenticatedUser);
        commentRepository.save(comment);

        return BaseApiResponse.success("Comment unliked");
    }

    @Override
    public BaseApiResponse<Long> countCommentLikes(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        Long commentLikeCount = (long) comment.getLikes().size();

        return BaseApiResponse.success(commentLikeCount, "Comment like count retrieved");
    }

}
