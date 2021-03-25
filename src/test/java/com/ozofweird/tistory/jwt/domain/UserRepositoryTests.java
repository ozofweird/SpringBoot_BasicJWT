package com.ozofweird.tistory.jwt.domain;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserRepositoryTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 회원_저장() {
        // given
        String adminEmail = "adminTest@example.com";
        userRepository.save(User.builder()
                .username("테스트1")
                .email(adminEmail)
                .password(passwordEncoder.encode("test1"))
                .role("ROLE_ADMIN")
                .active("1")
                .build()
        );

        String userEmail = "userTest@example.com";
        userRepository.save(User.builder()
                .username("테스트2")
                .email(userEmail)
                .password(passwordEncoder.encode("test2"))
                .role("ROLE_USER")
                .active("1")
                .build()
        );

        User admin = userRepository.findByEmail(adminEmail).orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));
        assertThat(admin.getEmail()).isEqualTo(adminEmail);

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));
        assertThat(user.getEmail()).isEqualTo(userEmail);
    }

}