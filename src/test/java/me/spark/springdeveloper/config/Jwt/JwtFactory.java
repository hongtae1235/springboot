package me.spark.springdeveloper.config.Jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import me.spark.springdeveloper.config.jwt.JwtProperties;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class JwtFactory {
    private String subject = "test@gmail.com";
    private Date issuedAt = new Date();
    private Date expiredAt = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
    private Map<String, Object> claims = Collections.emptyMap();

    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }

    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiredAt, Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiredAt = expiredAt != null ? expiredAt : this.expiredAt;
        this.claims = claims != null ? claims : this.claims;
    }

    public String createToken(JwtProperties jwtProperties) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .addClaims(claims)
                .signWith(
                        Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }
}
