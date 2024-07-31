package com.study.security6.domain.comment.controller;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    public String createComment(@RequestParam("content") String content, @RequestParam("boardId")Long boardId){
        commentService.createComment(boardId, null, content);
        return "redirect:/board/"+boardId.toString();
    }

    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable("id") Long id){
        commentService.deleteComments(id);
        return "redirect:/board";
    }

    @PutMapping("/{id}")
    public String updateBoardName(@PathVariable("id") Long id, @RequestParam("content") String content){
        commentService.updateComments(id, content);
        return "redirect:/board";
    }

}
