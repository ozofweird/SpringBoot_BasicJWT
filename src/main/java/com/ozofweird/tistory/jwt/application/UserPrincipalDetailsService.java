package com.ozofweird.tistory.jwt.application;

import com.ozofweird.tistory.jwt.domain.User;
import com.ozofweird.tistory.jwt.domain.UserPrincipal;
import com.ozofweird.tistory.jwt.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserPrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다."));
        return UserPrincipal.create(user);
    }

}
