package com.study.security6.security.authorization.method.comment.manager;

import lombok.Getter;

@Getter
public class CommentAuthorizeDto {
    private Long commentId;
    private Long boardId;
    private Long userId;

    public CommentAuthorizeDto(Long commentId, Long boardId, Long userId) {
        this.commentId = commentId;
        this.boardId = boardId;
        this.userId = userId;
    }
}
