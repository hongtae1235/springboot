package me.spark.springdeveloper.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt") // application.yml의 jwt로 시작하는 프로퍼티들을 매핑
public class JwtProperties {
    private String issuer;
    private String secretKey; // JWT 서명에 사용할 비밀 키
}
