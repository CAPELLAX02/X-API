package com.x.backend.services.post;

import com.x.backend.dto.post.request.CreatePostRequest;
import com.x.backend.dto.post.response.PostResponse;
import com.x.backend.models.entities.*;
import com.x.backend.repositories.ImageRepository;
import com.x.backend.repositories.PollOptionRepository;
import com.x.backend.repositories.PollRepository;
import com.x.backend.repositories.PostRepository;
import com.x.backend.services.image.PostImageService;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.builder.PostResponseBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final PollOptionRepository pollOptionRepository;
    private final PollRepository pollRepository;
    private final UserService userService;
    private final PostResponseBuilder postResponseBuilder;
    private final PostImageService postImageService;

    public PostServiceImpl(
            PostRepository postRepository,
            ImageRepository imageRepository,
            PollOptionRepository pollOptionRepository,
            PollRepository pollRepository,
            UserService userService,
            PostResponseBuilder postResponseBuilder,
            PostImageService postImageService) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.pollOptionRepository = pollOptionRepository;
        this.pollRepository = pollRepository;
        this.userService = userService;
        this.postResponseBuilder = postResponseBuilder;
        this.postImageService = postImageService;
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

        if (postImages != null && !postImages.isEmpty()) {
            List<Image> uploadedImages = postImageService.uploadPostImages(postImages);
            post.setMediaAttachments(uploadedImages);
        }

        if (req.hasPoll() && req.pollOptions() != null && req.pollExpiryDate() != null) {
            Poll poll = new Poll();
            poll.setPost(post);
            poll.setExpiresAt(req.pollExpiryDate());

            List<PollOption> options = new ArrayList<>();
            for (String optionText : req.pollOptions()) {
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

        Post savedPost = postRepository.save(post);
        PostResponse postResponse = postResponseBuilder.buildPostResponse(savedPost);
        return BaseApiResponse.success(postResponse, "Post created successfully.");
    }






























}
