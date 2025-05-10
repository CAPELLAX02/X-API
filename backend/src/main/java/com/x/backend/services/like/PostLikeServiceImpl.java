package com.x.backend.services.like;

import com.x.backend.exceptions.post.PostAlreadyLikedException;
import com.x.backend.exceptions.post.PostHaveNotLikedException;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.models.entities.Like;
import com.x.backend.models.entities.Post;
import com.x.backend.repositories.LikeRepository;
import com.x.backend.repositories.PostRepository;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.stereotype.Service;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public PostLikeServiceImpl(LikeRepository likeRepository,
                               PostRepository postRepository,
                               UserService userService)
    {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public BaseApiResponse<String> likePost(String username, Long postId) {
        ApplicationUser user = userService.getUserByUsername(username);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new PostAlreadyLikedException();
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

        return BaseApiResponse.success("Post liked successfully.");
    }

    @Override
    public BaseApiResponse<String> unlikePost(String username, Long postId) {
        ApplicationUser user = userService.getUserByUsername(username);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (!likeRepository.existsByUserAndPost(user, post)) {
            throw new PostHaveNotLikedException();
        }

        likeRepository.deleteByUserAndPost(user, post);
        return BaseApiResponse.success("Post unliked successfully.");
    }

    @Override
    public BaseApiResponse<Long> getLikeCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        long count = likeRepository.countByPost(post);
        return BaseApiResponse.success(count, "Post like count retrieved.");
    }

}
