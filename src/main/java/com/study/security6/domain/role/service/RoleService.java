package com.study.security6.domain.role.service;

import com.study.security6.domain.role.dto.RoleDto;
import com.study.security6.domain.role.entity.Role;
import com.study.security6.domain.role.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public List<RoleDto> getRoles(){
        return roleRepository.findAll().stream().map(RoleDto::convert).toList();
    }

    public RoleDto getRole(Long roleId){
        Role role = roleRepository.findById(roleId).orElseThrow(EntityNotFoundException::new);
        return RoleDto.convert(role);
    }

    @Transactional
    public void createRole(String roleName, String expression, boolean isBanned){
        Role role = new Role(roleName, expression, isBanned);
        roleRepository.save(role);
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
