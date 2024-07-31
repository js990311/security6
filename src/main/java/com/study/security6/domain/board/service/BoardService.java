package com.study.security6.domain.board.service;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.board.entity.Board;
import com.study.security6.domain.board.repository.BoardRepository;
import com.study.security6.domain.comment.dto.CommentDto;
import com.study.security6.domain.comment.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createBoard(String name){
        Board board = Board.builder().name(name).build();
        boardRepository.save(board);
    }

    public List<BoardDto> readAllBoards(){
        return boardRepository.findAll().stream()
                .map(board-> BoardDto.builder()
                        .id(board.getId())
                        .name(board.getName())
                        .comments(
                                board.getComments().stream()
                                        .map(comment->CommentDto.builder()
                                                .id(comment.getId())
                                                .parentId(comment.getParentId())
                                                .content(comment.getContent())
                                                .build()).toList()
                        )
                        .build())
                .toList();
    }

    public BoardDto readBoard(Long id){
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return BoardDto.builder()
                .id(board.getId())
                .name(board.getName())
                .comments(board.getComments().stream().map(CommentDto::convert).toList())
                .build();
    }

    @Transactional
    public void updateBoard(Long id, String updateName){
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        board.updateBoardName(updateName);
    }

    @Transactional
    public void deleteBoard(Long id){
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        boardRepository.delete(board);
    }
}
