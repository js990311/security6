package com.study.security6.domain.role.controller;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.board.service.BoardService;
import com.study.security6.domain.role.board.dto.BoardRoleDto;
import com.study.security6.domain.role.board.service.BoardRoleService;
import com.study.security6.domain.role.dto.RoleDto;
import com.study.security6.domain.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/manager")
@RequiredArgsConstructor
@Controller
public class BoardRoleController {
    private final RoleService roleService;
    private final BoardRoleService boardRoleService;
    private final BoardService boardService;

    @GetMapping("/{roleId}/boardRoles")
    public String getBoardRoles(@PathVariable("roleId") Long roleId, Model model){
        List<BoardRoleDto> boardRoles = boardRoleService.readBoardRoleByRoleId(roleId);
        RoleDto role = roleService.getRole(roleId);
        List<BoardDto> boards = boardService.readAllBoards();
        model.addAttribute("role", role);
        model.addAttribute("boardRoles", boardRoles);
        model.addAttribute("boards", boards);
        return "/manager/board/boardRoles";
    }

    @PostMapping("/{roleId}/boardRoles")
    public String addBoardRoles(@PathVariable("roleId") Long roleId, @RequestParam("boards") List<Long> boardIds){
        boardRoleService.addBoardRoles(roleId, boardIds);
        return "redirect:/manager/"+ roleId + "/boardRoles";
    }

    @DeleteMapping("/{roleId}/boardRoles/{boardRoleId}")
    public String deleteBoardRoles(
            @PathVariable("roleId") Long roleId,
            @PathVariable("boardRoleId") Long boardRoleId
    ){
        boardRoleService.deleteUserRole(boardRoleId);
        return "redirect:/manager/"+ roleId + "/boardRoles";
    }
}
