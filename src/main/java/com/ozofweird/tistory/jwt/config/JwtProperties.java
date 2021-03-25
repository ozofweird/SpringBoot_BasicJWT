package com.ozofweird.tistory.jwt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@Getter
@ConfigurationProperties(prefix = "app")
public class JwtProperties {

    private final Auth auth = new Auth();

    @Getter
    @Setter
    public static class Auth {
        private String tokenSecret;
        private int tokenExpiredDate;
        private String tokenPrefix;
        private String headerString;

        /**
         * Etc. 전송해야할 데이터의 크기가 증가하지만, 통신과정에서 바이너리 데이터 손실을 막기 위해 Base64로 인코딩
         *
         */
        public void encodeToBase64() {
            this.tokenSecret = Base64.getEncoder().encodeToString(this.tokenSecret.getBytes());
        }
    }
}
