package com.laliga.laliga_crud_app.config;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock JwtService jwtService;
    @Mock UserDetailsService userDetailsService;

    JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtService);
        // wstrzyknięcie mocka w pole z @Autowired
        ReflectionTestUtils.setField(filter, "userDetailsService", userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void doFilter(MockHttpServletRequest req) throws ServletException, IOException {
        var res = new MockHttpServletResponse();
        var chain = new MockFilterChain();
        filter.doFilter(req, res, chain);
    }

    @Test
    void noAuthorizationHeader_doesNotAuthenticate() throws Exception {
        var req = new MockHttpServletRequest("GET", "/api/test");

        doFilter(req);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(jwtService, userDetailsService);
    }

    @Test
    void headerWithoutBearerPrefix_doesNotAuthenticate() throws Exception {
        var req = new MockHttpServletRequest("GET", "/api/test");
        req.addHeader("Authorization", "Token abc.def.ghi");

        doFilter(req);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(jwtService, userDetailsService);
    }

    @Test
    void headerNotLookingLikeJwt_doesNotAuthenticate() throws Exception {
        var req = new MockHttpServletRequest("GET", "/api/test");
        // brak dwóch „.” → nie wygląda na JWT
        req.addHeader("Authorization", "Bearer not-a-jwt");

        doFilter(req);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(jwtService, userDetailsService);
    }

    @Test
    void malformedJwtException_doesNotAuthenticate() throws Exception {
        var req = new MockHttpServletRequest("GET", "/api/test");
        var badJwt = "aaa.bbb.ccc";
        req.addHeader("Authorization", "Bearer " + badJwt);

        when(jwtService.extractUsername(badJwt))
                .thenThrow(new io.jsonwebtoken.MalformedJwtException("bad"));

        doFilter(req);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(jwtService).extractUsername(badJwt);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void validToken_setsSecurityContext() throws Exception {
        var req = new MockHttpServletRequest("GET", "/api/test");
        var jwt = "aaa.bbb.ccc";
        req.addHeader("Authorization", "Bearer " + jwt);

        when(jwtService.extractUsername(jwt)).thenReturn("user@example.com");

        UserDetails user = new org.springframework.security.core.userdetails.User(
                "user@example.com", "hashed", List.of(new SimpleGrantedAuthority("USER"))
        );
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(user);
        when(jwtService.isTokenValid(jwt, user)).thenReturn(true);

        doFilter(req);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(auth.getName()).isEqualTo("user@example.com");
        assertThat(auth.getAuthorities()).extracting("authority").containsExactly("USER");

        verify(jwtService).extractUsername(jwt);
        verify(userDetailsService).loadUserByUsername("user@example.com");
        verify(jwtService).isTokenValid(jwt, user);
    }

    @Test
    void alreadyAuthenticated_skipsReloadingUser() throws Exception {
        // ustawiamy kontekst „wcześniej”
        var existing = new UsernamePasswordAuthenticationToken(
                "user@example.com", null, List.of(new SimpleGrantedAuthority("USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(existing);

        var req = new MockHttpServletRequest("GET", "/api/test");
        var jwt = "aaa.bbb.ccc";
        req.addHeader("Authorization", "Bearer " + jwt);

        // nawet jeśli nagłówek jest, filter nie powinien ponownie ładować usera
        when(jwtService.extractUsername(jwt)).thenReturn("user@example.com");

        doFilter(req);

        verify(jwtService).extractUsername(jwt);
        verifyNoInteractions(userDetailsService);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(existing);
    }
}