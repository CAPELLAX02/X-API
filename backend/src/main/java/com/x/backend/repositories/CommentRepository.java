package com.x.backend.repositories;

import com.x.backend.models.entities.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends BaseRepository<Comment, Long> {

    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);

    List<Comment> findByAuthorIdOrderByCreatedAtDesc(Long userId);

    long countByPostId(Long postId);

    @Query("SELECT c FROM Comment c WHERE LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY c.createdAt DESC")
    List<Comment> searchCommentsByKeyword(String keyword);
}
