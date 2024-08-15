package com.study.security6.domain.comment.controller;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.comment.service.CommentService;
import com.study.security6.security.authorization.method.CrudMethod;
import com.study.security6.security.authorization.method.comment.annotation.CommentPreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    @CommentPreAuthorize(method = CrudMethod.CREATE, commentId = "boardId")
    @PostMapping()
    public String createComment(@RequestParam("content") String content, @RequestParam("boardId")Long boardId, Authentication authentication){
        commentService.createComment(boardId, null, (Long) authentication.getPrincipal(),content);
        return "redirect:/board/"+boardId.toString();
    }

    @CommentPreAuthorize(method = CrudMethod.DELETE, commentId = "id")
    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable("id") Long id){
        commentService.deleteComments(id);
        return "redirect:/board";
    }

    @CommentPreAuthorize(method = CrudMethod.UPDATE, commentId = "id")
    @PutMapping("/{id}")
    public String updateBoardName(@PathVariable("id") Long id, @RequestParam("content") String content){
        commentService.updateComments(id, content);
        return "redirect:/board";
    }

}
