package com.ozofweird.tistory.jwt.config;

import com.ozofweird.tistory.jwt.domain.Role;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;

    /**
     * Etc. 객체 초기화, secretKey 를 Base64로 인코딩
     *
     */
    @PostConstruct
    protected void init() {
        jwtProperties.getAuth().encodeToBase64();
    }

    // JWT 토큰 생성
    public String createToken(String userPk, Role role) {
        // JWT Payload 에 저장되는 정보 단위
        Claims claims = Jwts.claims().setSubject(userPk);
        // 정보는 Key - Value 쌍으로 저장
        claims.put("role", role);

        Date now = new Date();
        return Jwts.builder()
                // 정보 저장
                .setClaims(claims)
                // 토큰 발행 시간 정보
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getAuth().getTokenExpiredDate()))
                // HS256 암호화 알고리즘을 이용한 서명 설정
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getAuth().getTokenSecret())
                .compact();
    }

    // JWT 토큰 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmailFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // JWT 토큰에서 사용자 정보(이메일) 추출
    public String getUserEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    // Request Header 에서 토큰 추출 ("X-AUTH-TOKEN" : "TOKEN")
    public String getJwtFromRequest(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // JWT 토큰 유효성/만료 검사
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtProperties.getAuth().getTokenSecret()).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SignatureException ex) {
            logger.error("유효하지 않은 JWT 서명");
        } catch (MalformedJwtException ex) {
            logger.error("유효하지 않은 JWT 토큰");
        } catch (ExpiredJwtException ex) {
            logger.error("만료된 JWT 토큰");
        } catch (UnsupportedJwtException ex) {
            logger.error("지원하지 않는 JWT 토큰");
        } catch (IllegalArgumentException ex) {
            logger.error("비어있는 JWT");
        }
        return false;
    }
}
