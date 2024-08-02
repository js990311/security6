package com.study.security6.domain.role.board.dto;

import com.study.security6.domain.role.board.entity.BoardRole;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BoardRoleDto {
    private Long id;
    private Long boardId;
    private String boardName;
    private Long roleId;
    private String roleName;


    public static BoardRoleDto convert(BoardRole boardRole){
        return BoardRoleDto.builder()
                .id(boardRole.getId())
                .boardId(boardRole.getBoardId())
                .boardName(boardRole.getBoard().getName())
                .roleId(boardRole.getRoleId())
                .roleName(boardRole.getRole().getRoleName())
                .build();
    }
}
