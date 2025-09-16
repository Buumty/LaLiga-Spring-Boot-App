package com.laliga.laliga_crud_app.config_test;

import com.laliga.laliga_crud_app.config.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private JwtService jwtService;

    // Skopiowany sekret z JwtService (musi być ten sam do podpisu!)
    private static final String SECRET_KEY = "u2Nf8pOb/9uLkEl4w3qYJbqhmV9A3WfKx7Q6VZ0Gl5Q=";

    private User userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        userDetails = new User("user@example.com", "hashed", List.of(() -> "USER"));
    }

    @Test
    void generateToken_andValidate_success() {
        String token = jwtService.generateToken(userDetails);

        assertThat(token).isNotBlank();
        assertThat(jwtService.extractUsername(token)).isEqualTo("user@example.com");
        assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    void extractUsername_returnsSubjectEmail() {
        String token = jwtService.generateToken(userDetails);
        assertThat(jwtService.extractUsername(token)).isEqualTo("user@example.com");
    }

    @Test
    void isTokenValid_falseForDifferentUser() {
        String token = jwtService.generateToken(userDetails);
        var other = new User("other@example.com", "x", List.of(() -> "USER"));

        assertThat(jwtService.isTokenValid(token, other)).isFalse();
    }
    
    @Test
    void parseOrValidate_throwsForBadSignature() {
        // Token podpisany INNYM sekretem
        SecretKey otherKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef"
        ));
        String forged = Jwts.builder()
                .subject("user@example.com")
                .issuedAt(java.util.Date.from(Instant.now()))
                .expiration(java.util.Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(otherKey, Jwts.SIG.HS256)
                .compact();

        // Twoje metody przy próbie parsowania rzucą wyjątek z JJWT (np. SignatureException/JwtException)
        assertThatThrownBy(() -> jwtService.extractUsername(forged))
                .isInstanceOf(RuntimeException.class); // lub .isInstanceOf(io.jsonwebtoken.JwtException.class)
    }
}