package com.study.security6.domain.role.board.repository;

import com.study.security6.domain.role.board.entity.BoardRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRoleRepository extends JpaRepository<BoardRole, Long> {
    List<BoardRole> findByBoardId(Long boardId);
    List<BoardRole> findByRoleId(Long roleId);

}
