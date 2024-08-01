package com.study.security6.domain.user.dto;

import com.study.security6.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {
    private Long id;
    private String username;

    public static UserDto convert(User user){
        return UserDto.builder()
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }
}
