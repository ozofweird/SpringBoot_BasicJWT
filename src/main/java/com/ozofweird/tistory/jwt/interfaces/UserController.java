package com.ozofweird.tistory.jwt.interfaces;

import com.ozofweird.tistory.jwt.application.UserService;
import com.ozofweird.tistory.jwt.domain.UserPrincipal;
import com.ozofweird.tistory.jwt.interfaces.dto.UserResponseDto;
import com.ozofweird.tistory.jwt.interfaces.dto.UserSignInRequestDto;
import com.ozofweird.tistory.jwt.interfaces.dto.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;

    // 회원 가입
    @PostMapping("/signUp")
    public Long signUp(@RequestBody UserSignUpRequestDto requestDto) {
        return userService.signUp(requestDto);
    }

    // 로그인
    @PostMapping("/signIn")
    public String signIn(@RequestBody UserSignInRequestDto requestDto) {
        return userService.signIn(requestDto);
    }

    // 회원 정보
    @GetMapping("/user")
    public UserResponseDto findById(@AuthenticationPrincipal UserPrincipal principal) {
        return userService.findByEmail(principal.getUsername());
    }

}
