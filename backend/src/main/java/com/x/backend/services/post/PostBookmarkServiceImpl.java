package com.x.backend.services.post;

import com.x.backend.exceptions.post.PostAlreadyBookmarkedException;
import com.x.backend.exceptions.post.PostHaveNotBookmarkedException;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.models.entities.Post;
import com.x.backend.repositories.PostRepository;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostBookmarkServiceImpl implements PostBookmarkService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostBookmarkServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    private Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    @Override
    public BaseApiResponse<String> bookmarkPost(String username, Long postId) {
        ApplicationUser user = userService.getUserByUsername(username);
        Post post = findPostById(postId);

        if (post.getBookmarks().contains(user)) throw new PostAlreadyBookmarkedException();

        post.getBookmarks().add(user);
        postRepository.save(post);

        return BaseApiResponse.success("Post bookmarked successfully");
    }

    @Override
    public BaseApiResponse<String> removeBookmark(String username, Long postId) {
        ApplicationUser user = userService.getUserByUsername(username);
        Post post = findPostById(postId);

        if (!post.getBookmarks().contains(user)) throw new PostHaveNotBookmarkedException();

        post.getBookmarks().remove(user);
        postRepository.save(post);

        return BaseApiResponse.success("Bookmark removed from post successfully");
    }

    @Override
    public BaseApiResponse<Boolean> hasUserBookmarked(String username, Long postId) {
        ApplicationUser user = userService.getUserByUsername(username);
        Post post = findPostById(postId);

        return BaseApiResponse.success(post.getBookmarks().contains(user));
    }

    @Override
    public BaseApiResponse<Long> getBookmarkCount(Long postId) {
        Post post = findPostById(postId);

        return BaseApiResponse.success((long) post.getBookmarks().size());
    }

}
