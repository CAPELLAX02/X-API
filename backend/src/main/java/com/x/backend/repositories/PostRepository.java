package com.x.backend.repositories;

import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.models.post.poll.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends BaseRepository<Post, Long> {

    long countByAuthorId(Long userId);

    List<Post> findAllByAuthorOrderByCreatedAtDesc(ApplicationUser author);
    List<Post> findAllByAuthorInOrderByCreatedAtDesc(List<ApplicationUser> authors);

    List<Post> findByOrderByCreatedAtDesc(Pageable pageable);

    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY SIZE(p.likes) DESC")
    List<Post> findMostLikedPosts(Pageable pageable);

    @EntityGraph(attributePaths = {"author", "likes", "comments"})
    @Query("SELECT p FROM Post p WHERE p.id = :postId")
    Optional<Post> findPostWithDetails(@Param("postId") Long postId);

    List<Post> findAllByAuthorOrderByCreatedAt(ApplicationUser author);
}
