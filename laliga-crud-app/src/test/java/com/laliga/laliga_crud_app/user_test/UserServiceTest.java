package com.laliga.laliga_crud_app.user_test;

import com.laliga.laliga_crud_app.user.UserMapper;
import com.laliga.laliga_crud_app.user.UserRepository;
import com.laliga.laliga_crud_app.user.UserService;
import com.laliga.laliga_crud_app.user.dto.UserCreateDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserMapper userMapper;
    @InjectMocks
    UserService userService;

    @BeforeAll
    private void setUp() {
        ArrayList<Object> users = new ArrayList<>();
        UserCreateDto userCreateDto = new UserCreateDto("Wojciech", "Andrzejczak", "wojtekand22@gmail.com", "secretpass");
        UserCreateDto userCreateDto1 = new UserCreateDto("Andrzej", "Wojciechowski", "andrzej211@gmail.com", "secretpass321");
        UserCreateDto userCreateDto2 = new UserCreateDto("Monika", "Gola", "monika221@gmail.com", "secretpass123");

        users.add(userCreateDto);
        users.add(userCreateDto1);
        users.add(userCreateDto2);
    }

    @Test
    void findAllUsers() {
        when(userService.f)
    }
}
