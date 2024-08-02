package com.study.security6.domain.user.service;

import com.study.security6.domain.user.dto.UserDto;
import com.study.security6.domain.user.repository.UserRepository;
import com.study.security6.domain.user.entity.User;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 유저 id와 password로 생성
     * @param username id
     * @param password 암호화되지 않는 password
     */
    public void createUser(String username, String password){
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);
    }

    public User readByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(NoResultException::new);
    }

    public UserDto readByUserId(Long userId){
        User user = userRepository.findById(userId).orElseThrow(NoResultException::new);
        return UserDto.convert(user);
    }

    public void updatePassword(Long userId, String password){
        User user = userRepository.findById(userId).orElseThrow(NoResultException::new);
        user.modifyPassword(passwordEncoder.encode(password));
    }

    public void deleteUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(NoResultException::new);
        userRepository.delete(user);
    }

    public List<UserDto> readAllUser(){
        return userRepository.findAll().stream().map(UserDto::convert).toList();
    }
}
