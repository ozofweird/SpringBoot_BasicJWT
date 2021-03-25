package com.ozofweird.tistory.jwt.config.security;

import com.ozofweird.tistory.jwt.config.JwtAuthenticationFilter;
import com.ozofweird.tistory.jwt.config.JwtTokenProvider;
import com.ozofweird.tistory.jwt.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Etc. 암호화에 필요한 PasswordEncoder Bean 등록 (new BCryptPasswordEncoder() 반환해도 무방)
     *
     * createDelegatingPasswordEncoder()
     * - 여러 개의 PasswordEncoder 유형 선언 뒤, 상황에 맞게 사용할 수 있도록 지원
     * - bcrypt(기본),noop, pbkdf2, scrypt, sha256
     * - 암호화 포맷 {id}encodedPassword (ex. {bcrypt}$2a$10$UemKUf.cijGeJz6CJ/81auJKQVU0syWTJq2O.UGQXga9G.SCCKDR.)
     *
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // AuthenticationManager Bean 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Etc. URL 더블 슬래시('//') 방지 (Spring Security 기본 정책)
     *
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.httpFirewall(defaultHttpFirewall());
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // REST API 서버에 불필요한 기본 설정 해제 (기본 로그인 창, csrf, 세션 비활성화)
                .httpBasic().disable()
                .cors().disable()
                .csrf().disable()
                // 로그인 폼 비활성화
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                // X-Frame-Option 헤더 비활성화 (clickjacking 공격 방지하기 위함이므로 h2-console 개발 환경에서만 적용)
                    .headers().frameOptions().disable()

                .and()
                // 경로에 대한 인증 요구
                    .authorizeRequests()
                        .antMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
                        .antMatchers("/api/user/**").hasRole(Role.USER.name())
                        .antMatchers("/**").permitAll()

                .and()
                // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 삽입
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                            UsernamePasswordAuthenticationFilter.class);
    }

}
