package com.ozofweird.tistory.jwt.application;

import com.ozofweird.tistory.jwt.config.JwtTokenProvider;
import com.ozofweird.tistory.jwt.domain.User;
import com.ozofweird.tistory.jwt.domain.UserRepository;
import com.ozofweird.tistory.jwt.interfaces.dto.UserResponseDto;
import com.ozofweird.tistory.jwt.interfaces.dto.UserSignUpRequestDto;
import com.ozofweird.tistory.jwt.interfaces.dto.UserSignInRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public Long signUp(UserSignUpRequestDto requestDto) {
        return userRepository.save(requestDto.toEntity()).getId();
    }

    public String signIn(UserSignInRequestDto requestDto) {
        User entity = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        if(!passwordEncoder.matches(requestDto.getPassword(), entity.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(entity.getEmail(), entity.getRole());
    }

    public UserResponseDto findByEmail(String email) {
        User entity = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        return new UserResponseDto(entity);
    }
}
