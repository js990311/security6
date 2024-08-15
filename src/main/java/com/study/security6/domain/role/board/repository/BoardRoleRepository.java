package com.study.security6.domain.role.board.repository;

import com.study.security6.domain.role.board.entity.BoardRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRoleRepository extends JpaRepository<BoardRole, Long> {
    List<BoardRole> findByBoardId(Long boardId);
    List<BoardRole> findByRoleId(Long roleId);

    @Query("select br FROM BoardRole br join fetch br.role r where r.isBanned = :isBanned")
    List<BoardRole> findByIsBanned(Boolean isBanned);

}
