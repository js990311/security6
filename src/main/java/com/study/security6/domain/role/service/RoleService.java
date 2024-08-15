package com.study.security6.domain.role.service;

import com.study.security6.domain.board.entity.Board;
import com.study.security6.domain.role.board.entity.BoardRole;
import com.study.security6.domain.role.board.repository.BoardRoleRepository;
import com.study.security6.domain.role.board.service.BoardRoleService;
import com.study.security6.domain.role.dto.RoleDto;
import com.study.security6.domain.role.entity.Role;
import com.study.security6.domain.role.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RoleService {
    private static String MANAGER_ROLE_PREFIX = "MANAGER_";
    private static String BANNED_ROLE_PREFIX = "BANNED_";

    private final RoleRepository roleRepository;
    private final BoardRoleService boardRoleService;
    private final BoardRoleRepository boardRoleRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void createBoard(Board board){
        String managerRoleName = MANAGER_ROLE_PREFIX.concat(board.getName());
        String bannedRoleName = BANNED_ROLE_PREFIX.concat(board.getName());

        Long managerId = createRole(managerRoleName,null, false);
        Long bannedId = createRole(bannedRoleName,null, true);

        boardRoleService.addBoardRole(board, managerId);
        boardRoleService.addBoardRole(board, bannedId);
    }

    public List<RoleDto> getRoles(){
        return roleRepository.findAll().stream().map(RoleDto::convert).toList();
    }

    public RoleDto getRole(Long roleId){
        Role role = roleRepository.findById(roleId).orElseThrow(EntityNotFoundException::new);
        return RoleDto.convert(role);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long createRole(String roleName, String expression, boolean isBanned){
        Role role = new Role(roleName, expression, isBanned);
        Long roleId = roleRepository.save(role).getId();
        return roleId;
    }

    @Transactional
    public void deleteRole(Long roleId){
        roleRepository.deleteById(roleId);
    }

    @Transactional
    public void updateName(Long roldId, String newRoleName){
        Role role = roleRepository.findById(roldId).orElseThrow(EntityNotFoundException::new);
        role.updateRoleName(newRoleName);
    }

    @Transactional
    public void updateExpression(Long roldId, String newExpression){
        Role role = roleRepository.findById(roldId).orElseThrow(EntityNotFoundException::new);
        role.updateRoleExpression(newExpression);
    }

}
