package com.study.security6.domain.board.controller;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.board.service.BoardService;
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

    @PostMapping()
    public String createBoard(@RequestParam("name") String name){
        boardService.createBoard(name);
        return "redirect:/board";
    }

    @GetMapping()
    public String getBoards(Model model){
        List<BoardDto> boards = boardService.readAllBoards();
        model.addAttribute("boards", boards);
        return "board/boards";
    }

    @GetMapping("/{id}")
    public String getBoard(@PathVariable("id") Long id, Model model){
        BoardDto board = boardService.readBoard(id);
        model.addAttribute("board", board);
        return "board/board";
    }

    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable("id") Long id){
        boardService.deleteBoard(id);
        return "redirect:/board";
    }

    @PutMapping("/{id}")
    public String updateBoardName(@PathVariable("id") Long id, @RequestParam("name") String name){
        boardService.updateBoard(id, name);
        return "redirect:/board";
    }
}
