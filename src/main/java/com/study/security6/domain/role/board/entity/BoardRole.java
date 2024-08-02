package com.study.security6.domain.role.board.entity;

import com.study.security6.domain.board.entity.Board;
import com.study.security6.domain.role.entity.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class BoardRole {
    @Id @GeneratedValue
    @Column(name = "board_role_id")
    private Long id;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    public BoardRole(Long roleId, Long boardId) {
        this.roleId = roleId;
        this.boardId = boardId;
    }
}
