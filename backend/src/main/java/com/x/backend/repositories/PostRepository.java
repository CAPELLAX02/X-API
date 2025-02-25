package com.x.backend.repositories;

import com.x.backend.models.entities.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends BaseRepository<Post, Long> {

    List<Post> findByOrderByCreatedAtDesc(Pageable pageable);

    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    long countByAuthorId(Long userId);

    @Query("SELECT p FROM Post p ORDER BY SIZE(p.likes) DESC")
    List<Post> findMostLikedPosts(Pageable pageable);

    @EntityGraph(attributePaths = {"author", "likes", "comments"})
    @Query("SELECT p FROM Post p WHERE p.id = :postId")
    Optional<Post> findPostWithDetails(@Param("postId") Long postId);
}
