package com.laliga.laliga_crud_app.auth_test;

import com.laliga.laliga_crud_app.auth.AuthenticationRequest;
import com.laliga.laliga_crud_app.auth.AuthenticationResponse;
import com.laliga.laliga_crud_app.auth.AuthenticationService;
import com.laliga.laliga_crud_app.auth.RegisterRequest;
import com.laliga.laliga_crud_app.user.Role;
import com.laliga.laliga_crud_app.user.User;
import com.laliga.laliga_crud_app.user.UserService;
import com.laliga.laliga_crud_app.user.dto.UserCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.EnumSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void givenCorrectRequest_whenRegister_thenCreatesUserAndReturnsJwt() {
        RegisterRequest registerRequest = new RegisterRequest("A", "B", "mail@gmail.com", "password");

        User user = new User();
        user.setId(42L);
        user.setFirstname("A");
        user.setLastname("B");
        user.setEmail("mail@gmail.com");
        user.setRoles(EnumSet.of(Role.USER));

        when(userService.createUser(any(UserCreateDto.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.register(registerRequest);

        assertThat(response.getToken()).isEqualTo("jwt-token");

        ArgumentCaptor<UserCreateDto> dtoCaptor = ArgumentCaptor.forClass(UserCreateDto.class);
        verify(userService).createUser(dtoCaptor.capture());
        UserCreateDto dto = dtoCaptor.getValue();
        assertThat(dto.firstname()).isEqualTo("A");
        assertThat(dto.lastname()).isEqualTo("B");
        assertThat(dto.email()).isEqualTo("mail@gmail.com");
        assertThat(dto.password()).isEqualTo("password");

        verify(jwtService).generateToken(user);
    }

    @Test
    void givenWrongEmail_whenRegister_thenThrowsException() {
        RegisterRequest registerRequest = new RegisterRequest("A", "B", "mail@gmail.com", "password");
        when(userService.createUser(any(UserCreateDto.class)))
                .thenThrow(new DataIntegrityViolationException("email unique violation"));

        assertThatThrownBy(() -> authenticationService.register(registerRequest))
                .isInstanceOf(DataIntegrityViolationException.class);

        verify(userService).createUser(any());
    }
    @Test
    void givenCorrectEmail_whenAuthenticate_thenReturnsJwt() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("mail@gmail.com","password");

        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));

        User user = new User();
        user.setId(42L);
        user.setEmail("mail@gmai.com");
        user.setRoles(EnumSet.of(Role.USER));

        when(userService.findUserByEmail(authenticationRequest.getEmail())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        assertThat(response.getToken()).isEqualTo("jwt-token");

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authCaptor
                = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(authCaptor.capture());
        UsernamePasswordAuthenticationToken token = authCaptor.getValue();
        assertThat(token.getPrincipal()).isEqualTo("mail@gmail.com");
        assertThat(token.getCredentials()).isEqualTo("password");

        verify(userService).findUserByEmail("mail@gmail.com");
        verify(jwtService).generateToken(user);
    }
    @Test
    void givenWrongEmail_whenAuthenticate_thenThrowsException() {
        AuthenticationRequest request = new AuthenticationRequest("mail@gmail.com", "wrong_password");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThatThrownBy(() -> authenticationService.authenticate(request))
                .isInstanceOf(BadCredentialsException.class);

        verify(authenticationManager).authenticate(any());
    }
}
