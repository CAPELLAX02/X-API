package com.x.backend.repositories;

import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.models.post.Like;
import com.x.backend.models.post.Post;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends BaseRepository<Like, Long> {

    boolean existsByUserAndPost(ApplicationUser user, Post post);

    Optional<Like> findByUserAndPost(ApplicationUser user, Post post);

    long countByPost(Post post);

    void deleteByUserAndPost(ApplicationUser user, Post post);

}
