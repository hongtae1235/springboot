package me.spark.springdeveloper.config.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me.spark.springdeveloper.config.jwt.JwtProperties;
import me.spark.springdeveloper.config.jwt.TokenProvider;
import me.spark.springdeveloper.dao.User;
import me.spark.springdeveloper.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @DisplayName("generateToken: user info and expiration create a token")
    @Test
    void generateToken() {
        User testUser = userRepository.save(User.builder()
                .email("test@gmail.com")
                .password("test")
                .build());

        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        Long userId = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)))
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("validateToken: 유효한 토큰인 경우에 유효성 검증에 성공")
    @Test
    void validateToken_validToken() {
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);

        boolean result = tokenProvider.validToken(token);

        assertThat(result).isTrue();
    }

    @DisplayName("validateToken: 만료된 토큰인 경우에 유효성 검증이 실패")
    @Test
    void validateToken_invalidToken() {
        String expiredToken = JwtFactory.builder()
                .expiredAt(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        boolean result = tokenProvider.validToken(expiredToken);

        assertThat(result).isFalse();
    }
}
