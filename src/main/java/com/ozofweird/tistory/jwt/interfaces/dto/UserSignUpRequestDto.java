package com.ozofweird.tistory.jwt.interfaces.dto;

import com.ozofweird.tistory.jwt.domain.Role;
import com.ozofweird.tistory.jwt.domain.User;
import lombok.Getter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Getter
public class UserSignUpRequestDto {

    private String email;
    private String password;
    private String username;

    public User toEntity() {

        return User.builder()
                .email(email)
                .password(
                        /**
                         * Etc. {bcrypt}encodedPassword 형태로 전달
                         *
                         */
                        PasswordEncoderFactories
                                .createDelegatingPasswordEncoder()
                                .encode(password))
                .username(username)
                .active(1)
                .role(Role.USER)
                .build();
    }

}
