package com.x.backend.repositories;

import com.x.backend.models.post.comment.Comment;
import com.x.backend.models.post.comment.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCommentRepository extends JpaRepository<SubComment, Long> {

    List<SubComment> findAllByParentCommentOrderByCreatedAtDeleted(Comment parentComment);

}
