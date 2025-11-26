package com.laliga.laliga_crud_app.auth;

import com.laliga.laliga_crud_app.config.JwtUtil;
import com.laliga.laliga_crud_app.entities.user.UserService;
import com.laliga.laliga_crud_app.entities.user.dto.UserCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthenticationService(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = userService.createUser(new UserCreateDto(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                request.getPassword()
        ));
        var jwtToken = jwtUtil.generateToken(user.getEmail());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userService.findUserByEmail(request.getEmail());
        var jwtToken = jwtUtil.generateToken(user.getEmail());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
