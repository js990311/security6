package com.study.security6.domain.comment.repository;

import com.study.security6.domain.comment.entity.Comment;
import com.study.security6.security.authorization.method.comment.manager.CommentAuthorizeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select new com.study.security6.security.authorization.method.comment.manager.CommentAuthorizeDto(c.id, c.boardId, c.userId) " +
            "from Comment c " +
            "where c.id = :commentId")
    CommentAuthorizeDto findCommntAOByCommentId(Long commentId);
}
