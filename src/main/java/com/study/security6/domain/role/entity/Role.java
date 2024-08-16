package com.study.security6.domain.role.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "roles")
@Entity
public class Role {
    @Id @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column
    private String expression;

    @Column
    private boolean isBanned;

    public Role(String roleName, String expression, boolean isBanned) {
        this.roleName = roleName;
        this.expression = expression;
        this.isBanned = isBanned;
        if(this.expression == null){
            this.expression = "";
        }
    }

    public void updateRoleName(String roleName){
        this.roleName = roleName;
    }

    public void addHighRole(String high){
        StringBuilder sb = new StringBuilder();
        if(this.expression != null){
            sb.append(this.expression);
        }
        sb.append("ROLE_").append(high).append(" > ").append("ROLE_").append(roleName).append("\n");
        this.expression = sb.toString();
    }

    public void addRowRole(String row){
        StringBuilder sb = new StringBuilder();
        if(this.expression != null){
            sb.append(this.expression);
        }
        sb.append("ROLE_").append(roleName).append(" > ").append("ROLE_").append(row).append("\n");
        this.expression = sb.toString();
    }
}
