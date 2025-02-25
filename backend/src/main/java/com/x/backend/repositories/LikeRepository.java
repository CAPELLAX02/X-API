package com.x.backend.repositories;

import com.x.backend.models.entities.Like;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends BaseRepository<Like, Long> {

    List<Like> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByPostId(Long postId);

    boolean existsByUserIdAndPostId(Long userId, Long postId);
}
