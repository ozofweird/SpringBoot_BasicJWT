package com.ozofweird.tistory.jwt.config;

import com.ozofweird.tistory.jwt.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // Request Header 에서 토큰 추출
            String token = jwtTokenProvider.getJwtFromRequest((HttpServletRequest) request);
            // JWT 토큰 유효성/만료 검사
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰이 유효할 경우, 토큰에서 회원 정보 추출
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                // SecurityContext 에 Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e){
            e.printStackTrace();
            logger.error("Security Context에서 사용자 인증을 설정할 수 없습니다.");
        }
        chain.doFilter(request, response);
    }

}
