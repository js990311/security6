package com.study.security6.domain.user.service;

import com.study.security6.domain.role.user.dto.UserRoleDto;
import com.study.security6.domain.role.user.service.UserRoleService;
import com.study.security6.domain.user.dto.ProviderUser;
import com.study.security6.domain.user.dto.UserDto;
import com.study.security6.domain.user.repository.UserRepository;
import com.study.security6.domain.user.entity.User;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleService userRoleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 유저 id와 password로 생성
     * @param username id
     * @param password 암호화되지 않는 password
     */
    @Transactional
    public void createUser(String username, String password){
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        Long userId = userRepository.save(user).getId();
        userRoleService.createUser(userId);
    }

    @Transactional
    public void createUser(ProviderUser providerUser){
        User user = User.builder()
                .username(providerUser.getEmail())
                .password(null)
                .provider(providerUser.getProvider())
                .build();
        Long id = userRepository.save(user).getId();
        userRoleService.createUser(id);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public List<UserRoleDto> findUserRoleByUsername(String username){
        User user = userRepository.findByUsername(username).orElseThrow();
        return userRoleService.readUserRoleByUserId(user.getId());
    }

    public User readByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(NoResultException::new);
    }

    public UserDto readByUserId(Long userId){
        User user = userRepository.findById(userId).orElseThrow(NoResultException::new);
        return UserDto.convert(user);
    }

    @Transactional
    public void updatePassword(Long userId, String password){
        User user = userRepository.findById(userId).orElseThrow(NoResultException::new);
        user.modifyPassword(passwordEncoder.encode(password));
    }

    @Transactional
    public void deleteUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(NoResultException::new);
        userRepository.delete(user);
    }

    public List<UserDto> readAllUser(){
        return userRepository.findAll().stream().map(UserDto::convert).toList();
    }
}
