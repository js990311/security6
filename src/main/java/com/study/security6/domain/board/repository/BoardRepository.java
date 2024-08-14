package com.study.security6.domain.board.repository;

import com.study.security6.domain.board.entity.Board;
import com.study.security6.security.authorization.method.comment.manager.CommentAuthorizeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
