package com.x.backend.repositories;

import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.author " +
            "LEFT JOIN FETCH p.replies")
    List<Post> findAllWithRelations();

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.author " +
            "LEFT JOIN FETCH p.replies " +
            "WHERE p.postId = :id")
    Optional<Post> findByIdWithRelations(@Param("id") Integer id);

    @Query("SELECT p FROM Post p WHERE p.author = :author")
    Set<Post> findByAuthor(@Param("author") ApplicationUser author);

}
