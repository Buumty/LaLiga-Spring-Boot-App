package com.laliga.laliga_crud_app.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();

        // Ustawiamy sekretny klucz i czas życia tokenu "ręcznie"
        Field secretField = JwtUtil.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        // musi być wystarczająco długie dla HS512
        secretField.set(jwtUtil, "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF");
        Field expirationField = JwtUtil.class.getDeclaredField("jwtExpirationMs");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtil, 3600000L); // 1h

        // wywołanie @PostConstruct
        jwtUtil.init();
    }

    @Test
    void generateToken_shouldReturnValidTokenWithGivenEmailAsSubject() {
        // given
        String email = "user@mail.com";

        // when
        String token = jwtUtil.generateToken(email);

        // then
        assertThat(token).isNotNull().isNotBlank();
        assertThat(jwtUtil.validateJwtToken(token)).isTrue();
        assertThat(jwtUtil.getUserFromToken(token)).isEqualTo(email);
    }

    @Test
    void validateJwtToken_shouldReturnFalseForInvalidToken() {
        // given
        String email = "user@mail.com";
        String validToken = jwtUtil.generateToken(email);
        String tamperedToken = validToken + "abc"; // psujemy podpis

        // when
        boolean result = jwtUtil.validateJwtToken(tamperedToken);

        // then
        assertThat(result).isFalse();
    }
}
