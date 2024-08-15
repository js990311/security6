package com.study.security6.domain.role.board.service;

import com.study.security6.domain.role.board.dto.BoardRoleDto;
import com.study.security6.domain.role.board.entity.BoardRole;
import com.study.security6.domain.role.board.repository.BoardRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardRoleService {
    private final BoardRoleRepository boardRoleRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void addBoardRole(Long boardId, Long roleId){
        BoardRole boardRole = new BoardRole(boardId, roleId);
        boardRoleRepository.save(boardRole);
    }

    @Transactional
    public void addBoardRoles(Long roldId, List<Long> boardIds){
        List<BoardRole> boardRoles = boardIds.stream().map(boardId -> new BoardRole(roldId, boardId)).toList();
        boardRoleRepository.saveAll(boardRoles);
    }

    @Transactional
    public void deleteUserRole(Long boardRoleId){
        boardRoleRepository.deleteById(boardRoleId);
    }

    public List<BoardRoleDto> readBoardRoleByBoardId(Long boardId){
        List<BoardRole> boardRoles = boardRoleRepository.findByBoardId(boardId);
        return boardRoles.stream().map(BoardRoleDto::convert).toList();
    }

    public List<BoardRoleDto> readBoardRoleByRoleId(Long roleId){
        List<BoardRole> boardRoles = boardRoleRepository.findByRoleId(roleId);
        return boardRoles.stream().map(BoardRoleDto::convert).toList();
    }

    public List<BoardRoleDto> readAllBoardRole(){
        return boardRoleRepository.findAll().stream().map(BoardRoleDto::convert).toList();
    }

    public List<BoardRoleDto> findBoardRoleByisBanned(boolean isBanned){
        return boardRoleRepository.findByIsBanned(isBanned).stream().map(BoardRoleDto::convert).toList();
    }

}
