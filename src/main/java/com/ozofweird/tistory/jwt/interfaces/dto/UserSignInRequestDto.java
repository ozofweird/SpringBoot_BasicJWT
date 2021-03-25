package com.ozofweird.tistory.jwt.interfaces.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignInRequestDto {

    private String email;
    private String password;

    @Builder
    public UserSignInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
