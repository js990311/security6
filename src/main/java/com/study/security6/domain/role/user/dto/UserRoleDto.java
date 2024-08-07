package com.study.security6.domain.role.user.dto;

import com.study.security6.domain.role.user.entity.UserRole;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRoleDto {
    private Long id;
    private Long roleId;
    private String roleName;

    public static UserRoleDto convert(UserRole userRole){
        return UserRoleDto.builder()
                .id(userRole.getId())
                .roleId(userRole.getRole().getId())
                .roleName(userRole.getRole().getRoleName())
                .build();
    }
}
