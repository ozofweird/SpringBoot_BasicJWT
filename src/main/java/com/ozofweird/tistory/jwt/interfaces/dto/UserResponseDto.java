package com.ozofweird.tistory.jwt.interfaces.dto;

import com.ozofweird.tistory.jwt.domain.Role;
import com.ozofweird.tistory.jwt.domain.User;
import lombok.Getter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Getter
public class UserResponseDto {

    private Long id;
    private String email;
    private String username;
    private Role role;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
