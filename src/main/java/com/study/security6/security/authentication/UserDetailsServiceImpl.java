package com.study.security6.security.authentication;

import com.study.security6.domain.role.user.dto.UserRoleDto;
import com.study.security6.domain.role.user.service.UserRoleService;
import com.study.security6.domain.user.entity.User;
import com.study.security6.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;

    public UserDetailsServiceImpl(UserRepository userRepository, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username));
        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(User user){
        String[] userRoles = userRoleService.readUserRoleByUserId(user.getId()).stream().map(UserRoleDto::getRoleName).toArray(String[]::new);
        return org.springframework.security.core.userdetails.User.builder()
                .username(String.valueOf(user.getId())) // Authorization logic 편의상 user_id로 변경
                .password(user.getPassword())
                .roles(userRoles)
                .build();
    }
}
