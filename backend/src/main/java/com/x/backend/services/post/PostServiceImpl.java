package com.x.backend.services.post;

import com.x.backend.dto.CreatePostRequest;
import com.x.backend.exceptions.PostNotFoundException;
import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Post;
import com.x.backend.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post createPost(CreatePostRequest createPostRequest) {
        Post post = new Post();

        if (createPostRequest.scheduled()) {
            post.setPostedDate(createPostRequest.scheduledDate());
        } else {
            post.setPostedDate(LocalDateTime.now());
        }

        post.setContent(createPostRequest.content());
        post.setAuthor(createPostRequest.author());
        post.setReplies(createPostRequest.replies());
        post.setScheduled(createPostRequest.scheduled());
        post.setScheduledDate(createPostRequest.scheduledDate());
        post.setAudience(createPostRequest.audience());
        post.setReplyRestriction(createPostRequest.replyRestriction());

        try {
            return postRepository.save(post);
        } catch (Exception e) {
            // TODO: custom exception
            return null;
        }
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @Override
    public Set<Post> getAllPostsByAuthor(ApplicationUser author) {
        return postRepository.findByAuthor(author)
                .orElse(new HashSet<>());
    }

    @Override
    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }

}
