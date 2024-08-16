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
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RoleService {
    private static String ROLE_USER = "USER";
    private static String ROLE_ADMIN = "ADMIN";
    private static String ROLE_GLOBAL_BANNED = "GLOBAL_BANNED";
    private static String MANAGER_ROLE_PREFIX = "MANAGER_";
    private static String BANNED_ROLE_PREFIX = "BANNED_";

    private final RoleRepository roleRepository;
    private final BoardRoleService boardRoleService;

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
        if(isBanned){
            role.addHighRole(ROLE_GLOBAL_BANNED);
        }else {
            role.addHighRole(ROLE_ADMIN);
            role.addRowRole(ROLE_USER);
        }
        Long roleId = roleRepository.save(role).getId();
        return roleId;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String createGlobalRole(){
        Role admin = new Role(ROLE_ADMIN, null, false);
        Role user = new Role(ROLE_USER, null, false);
        Role globalBanned = new Role(ROLE_GLOBAL_BANNED, null, false);

        user.addHighRole(ROLE_ADMIN);
        user.addRowRole(ROLE_GLOBAL_BANNED);

        String expression = user.getExpression();

        roleRepository.save(admin);
        roleRepository.save(user);
        roleRepository.save(globalBanned);

        return expression;
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
    public String getRoleHierarchy(){
        List<String> hierarchies = roleRepository.findAllRoleHierarchies();
        String roleHierarchy = null;
        if(hierarchies == null || hierarchies.isEmpty()){
            roleHierarchy = createGlobalRole();
        }else {
           StringBuilder sb = new StringBuilder();
           for(String hierarcy : hierarchies){
               sb.append(hierarcy);
           }
           roleHierarchy = sb.toString();
        }
        return roleHierarchy;
    }
}
