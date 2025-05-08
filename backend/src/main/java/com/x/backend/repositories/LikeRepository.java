package com.x.backend.repositories;

import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.models.entities.Like;
import com.x.backend.models.entities.Post;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends BaseRepository<Like, Long> {

    boolean existsByUserAndPost(ApplicationUser user, Post post);

    Optional<Like> findByUserAndPost(ApplicationUser user, Post post);

    long countByPost(Post post);

    void deleteByUserAndPost(ApplicationUser user, Post post);

}
