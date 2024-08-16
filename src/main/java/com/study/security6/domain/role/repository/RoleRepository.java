package com.study.security6.domain.role.repository;

import com.study.security6.domain.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);

    @Query("SELECT r.expression FROM Role r")
    List<String> findAllRoleHierarchies();
}
