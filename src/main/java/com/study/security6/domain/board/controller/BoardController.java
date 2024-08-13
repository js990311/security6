package com.study.security6.domain.board.controller;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.board.service.BoardService;
import com.study.security6.security.authorization.method.annotation.BoardAuthorization;
import com.study.security6.security.authorization.method.annotation.CrudMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;

    @BoardAuthorization(method=CrudMethod.CREATE)
    @PostMapping()
    public String createBoard(@RequestParam("name") String name){
        boardService.createBoard(name);
        return "redirect:/board";
    }

    @BoardAuthorization(method=CrudMethod.READ)
    @GetMapping()
    public String getBoards(Model model){
        List<BoardDto> boards = boardService.readAllBoards();
        model.addAttribute("boards", boards);
        return "board/boards";
    }

    @BoardAuthorization(method=CrudMethod.READ, boardId = "id")
    @GetMapping("/{id}")
    public String getBoard(@PathVariable("id") Long id, Model model){
        BoardDto board = boardService.readBoard(id);
        model.addAttribute("board", board);
        return "board/board";
    }

    @BoardAuthorization(method = CrudMethod.DELETE, boardId = "id")
    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable("id") Long id){
        boardService.deleteBoard(id);
        return "redirect:/board";
    }

    @BoardAuthorization(method = CrudMethod.UPDATE, boardId = "id")
    @PutMapping("/{id}")
    public String updateBoardName(@PathVariable("id") Long id, @RequestParam("name") String name){
        boardService.updateBoard(id, name);
        return "redirect:/board";
    }
}
