package com.study.security6.domain.role.dto;

import com.study.security6.domain.role.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoleDto {
    private Long roleId;
    private String name;
    private String expression;

    public static RoleDto convert(Role role){
        return RoleDto.builder()
                .roleId(role.getId())
                .name(role.getRoleName())
                .expression(role.getExpression())
                .build();
    }
}
