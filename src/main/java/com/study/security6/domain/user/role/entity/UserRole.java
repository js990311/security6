package com.study.security6.domain.user.role.entity;

import com.study.security6.domain.role.entity.Role;
import com.study.security6.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class UserRole {
    @Id @GeneratedValue
    private Long id;

    @Column(name = "userId")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", updatable = false, insertable = false)
    private User user;

    @Column(name = "roleId")
    private Long roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", updatable = false, insertable = false)
    private Role role;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
