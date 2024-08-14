package com.study.security6.domain.comment.service;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.board.entity.Board;
import com.study.security6.domain.comment.dto.CommentDto;
import com.study.security6.domain.comment.entity.Comment;
import com.study.security6.domain.comment.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public void createComment(Long boardId, Long parentId, Long userId, String content){
        Comment comment = Comment.builder()
                .boardId(boardId)
                .parentId(parentId)
                .userId(userId)
                .content(content)
                .build();
        commentRepository.save(comment);
    }


    public List<CommentDto> readComments(){
        return commentRepository
                .findAll().stream()
                .map(CommentDto::convert).toList();
    }

    @Transactional
    public void updateComments(Long id, String updateContent){
        Comment comment = commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        comment.updateContent(updateContent);
    }

    @Transactional
    public void deleteComments(Long id){
        commentRepository.deleteById(id);
    }
}
