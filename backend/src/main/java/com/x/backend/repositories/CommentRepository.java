package com.x.backend.repositories;

import com.x.backend.models.entities.Comment;
import com.x.backend.models.entities.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends BaseRepository<Comment, Long> {

    List<Comment> findAllByPostOrderByCreatedAtDesc(Post post);
}
