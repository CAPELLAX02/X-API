package com.x.backend.services.post;

import com.x.backend.exceptions.post.PostAlreadyRepostedException;
import com.x.backend.exceptions.post.PostHaveNotRepostedException;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.post.Post;
import com.x.backend.repositories.PostRepository;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostRepostServiceImpl implements PostRepostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostRepostServiceImpl(PostRepository postRepository,
                                 UserService userService
    ) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public BaseApiResponse<String> repostPost(String username, Long postId) {
        ApplicationUser user = userService.getUserByUsername(username);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        if (post.getReposts().contains(user)) throw new PostAlreadyRepostedException();

        post.getReposts().add(user);
        postRepository.save(post);

        return BaseApiResponse.success("Post reposted successfully");
    }

    @Override
    public BaseApiResponse<String> undoRepost(String username, Long postId) {
        ApplicationUser user = userService.getUserByUsername(username);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        if (!post.getReposts().contains(user)) throw new PostHaveNotRepostedException();

        post.getReposts().remove(user);
        postRepository.save(post);

        return BaseApiResponse.success("Repost undone successfully");
    }

    @Override
    public BaseApiResponse<Long> getRepostCount(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        Long repostCount = (long) post.getReposts().size();

        return BaseApiResponse.success(repostCount, "Repost count retrieved");
    }

    @Override
    public BaseApiResponse<Boolean> hasUserReposted(String username, Long postId) {
        ApplicationUser user = userService.getUserByUsername(username);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return BaseApiResponse.success(post.getReposts().contains(user));
    }

}
