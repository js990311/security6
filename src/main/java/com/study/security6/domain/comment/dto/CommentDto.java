package com.study.security6.domain.comment.dto;

import com.study.security6.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentDto {
    private Long id;
    private String content;
    private Long parentId;
    private Long boardId;
    public static CommentDto convert(Comment comment){
        return builder()
                .id(comment.getId())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .boardId(comment.getBoardId())
                .build();
    }
}
