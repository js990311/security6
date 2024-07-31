package com.study.security6.domain.board.dto;

import com.study.security6.domain.comment.dto.CommentDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BoardDto {
    private Long id;
    private String name;
    List<CommentDto> comments;
}
