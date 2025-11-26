package com.laliga.laliga_crud_app.auth;

import com.laliga.laliga_crud_app.config.JwtUtil;
import com.laliga.laliga_crud_app.entities.user.User;
import com.laliga.laliga_crud_app.entities.user.UserService;
import com.laliga.laliga_crud_app.entities.user.dto.UserCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void givenRegisterRequest_whenRegister_thenCreateUserGenerateTokenAndReturnResponse() {
        // given
        RegisterRequest request = mock(RegisterRequest.class);
        when(request.getFirstname()).thenReturn("John");
        when(request.getLastname()).thenReturn("Doe");
        when(request.getEmail()).thenReturn("john.doe@mail.com");
        when(request.getPassword()).thenReturn("password123");

        User createdUser = new User();
        createdUser.setEmail("john.doe@mail.com");

        when(userService.createUser(any(UserCreateDto.class))).thenReturn(createdUser);
        when(jwtUtil.generateToken(createdUser.getEmail())).thenReturn("jwt-token-123");

        // when
        AuthenticationResponse response = authenticationService.register(request);

        // then
        // 1. sprawdzamy, że createUser dostał poprawne dane
        ArgumentCaptor<UserCreateDto> dtoCaptor = ArgumentCaptor.forClass(UserCreateDto.class);
        verify(userService).createUser(dtoCaptor.capture());
        UserCreateDto usedDto = dtoCaptor.getValue();

        assertThat(usedDto.firstname()).isEqualTo("John");
        assertThat(usedDto.lastname()).isEqualTo("Doe");
        assertThat(usedDto.email()).isEqualTo("john.doe@mail.com");
        assertThat(usedDto.password()).isEqualTo("password123");

        // 2. sprawdzamy wywołanie JwtUtil
        verify(jwtUtil).generateToken("john.doe@mail.com");

        // 3. sprawdzamy odpowiedź
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt-token-123");

        // 4. authManager nie powinien być tu użyty
        verifyNoInteractions(authenticationManager);
    }

    @Test
    void givenAuthenticationRequest_whenAuthenticate_thenAuthenticateUserGenerateTokenAndReturnResponse() {
        // given
        AuthenticationRequest request = mock(AuthenticationRequest.class);
        when(request.getEmail()).thenReturn("user@mail.com");
        when(request.getPassword()).thenReturn("password123");

        User user = new User();
        user.setEmail("user@mail.com");

        when(userService.findUserByEmail("user@mail.com")).thenReturn(user);
        when(jwtUtil.generateToken("user@mail.com")).thenReturn("jwt-token-xyz");

        // when
        AuthenticationResponse response = authenticationService.authenticate(request);

        // then
        // 1. sprawdzamy wywołanie authenticationManager.authenticate(...)
        ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        verify(authenticationManager).authenticate(tokenCaptor.capture());
        UsernamePasswordAuthenticationToken authToken = tokenCaptor.getValue();

        assertThat(authToken.getPrincipal()).isEqualTo("user@mail.com");
        assertThat(authToken.getCredentials()).isEqualTo("password123");

        // 2. sprawdzamy współpracę z UserService i JwtUtil
        verify(userService).findUserByEmail("user@mail.com");
        verify(jwtUtil).generateToken("user@mail.com");

        // 3. sprawdzamy odpowiedź
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt-token-xyz");
    }
}
