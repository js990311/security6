package com.study.security6.domain.role.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Role {
    @Id @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column
    private String expression;

    public Role(String roleName, String expression) {
        this.roleName = roleName;
        this.expression = expression;
    }

    public void updateRoleName(String roleName){
        this.roleName = roleName;
    }

    public void updateRoleExpression(String roleExpression){
        this.expression = expression;
    }
}
