package com.study.security6.domain.role.user.service;

import com.study.security6.domain.role.entity.Role;
import com.study.security6.domain.role.repository.RoleRepository;
import com.study.security6.domain.role.user.repository.UserRoleRepository;
import com.study.security6.domain.role.user.dto.UserRoleDto;
import com.study.security6.domain.role.user.entity.UserRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void createUser(Long userId){
        Role roleUser = roleRepository.findByRoleName("USER").orElseThrow(EntityNotFoundException::new);
        UserRole userRole = new UserRole(userId, roleUser.getId());
        userRoleRepository.save(userRole);
    }

    @Transactional
    public void addUserRole(Long userId, Long roleId){
        UserRole userRole = new UserRole(userId, roleId);
        userRoleRepository.save(userRole);
    }

    @Transactional
    public void addUserRoles(Long userId, List<Long> roleIds){
        List<UserRole> userRoles = roleIds.stream().map(roleId -> new UserRole(userId, roleId)).toList();
        userRoleRepository.saveAll(userRoles);
    }

    @Transactional
    public void deleteUserRole(Long userRoleId){
        userRoleRepository.deleteById(userRoleId);
    }

    public List<UserRoleDto> readUserRoleByUserId(Long userId){
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        return userRoles.stream().map(UserRoleDto::convert).toList();
    }

}
