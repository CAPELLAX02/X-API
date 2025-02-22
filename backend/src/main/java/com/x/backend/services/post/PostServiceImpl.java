package com.x.backend.services.post;

import com.x.backend.dto.CreatePostRequest;
import com.x.backend.exceptions.PostNotFoundException;
import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Post;
import com.x.backend.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Post createPost(CreatePostRequest createPostRequest) {
        Post post = new Post();
        post.setPostedDate(
                createPostRequest.scheduled() ?
                createPostRequest.scheduledDate() :
                LocalDateTime.now()
        );
        post.setContent(createPostRequest.content());
        post.setAuthor(createPostRequest.author());
        post.setScheduled(createPostRequest.scheduled());
        post.setScheduledDate(createPostRequest.scheduledDate());
        post.setAudience(createPostRequest.audience());
        post.setReplyRestriction(createPostRequest.replyRestriction());
        return postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAllWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPostById(Integer id) {
        return postRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPostsByAuthor(ApplicationUser author) {
        Set<Post> userPosts = postRepository.findByAuthor(author);
        List<Post> sortedPosts = new ArrayList<>(userPosts);
        sortedPosts.sort(Comparator.comparing(Post::getPostedDate).reversed());
        return sortedPosts;
    }

    @Override
    public void deletePost(Integer id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }

}
