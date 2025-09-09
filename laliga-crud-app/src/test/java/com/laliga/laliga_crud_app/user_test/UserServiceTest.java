package com.laliga.laliga_crud_app.user_test;

import com.laliga.laliga_crud_app.user.Role;
import com.laliga.laliga_crud_app.user.User;
import com.laliga.laliga_crud_app.user.UserRepository;
import com.laliga.laliga_crud_app.user.UserService;
import com.laliga.laliga_crud_app.user.dto.UserCreateDto;
import com.laliga.laliga_crud_app.user.dto.UserReadDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;



    @Test
    void givenNoUsersDto_whenFindAllUsersDto_thenReturnEmptyList() {
       when(userRepository.findAll()).thenReturn(emptyList());
        List<UserReadDto> allUsersDto = userService.findAllUsersDto();

        assertNotNull(allUsersDto);
        assertEquals(0, allUsersDto.size(), "List size should be 0");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void givenOneUserDto_whenFindAllUsersDto_thenReturnListOfOneObjectOfUserReadDtoClass() {
        when(userRepository.findAll()).thenReturn(List.of(new User()));
        List<UserReadDto> allUsersDto = userService.findAllUsersDto();

        assertNotNull(allUsersDto);
        assertEquals(1, allUsersDto.size(), "List size should be 1");
        assertEquals(allUsersDto.getFirst().getClass(), UserReadDto.class, "Object should be of UserReadDto class");
        verify(userRepository,times(1)).findAll();
    }

    @Test
    void givenUserCreateDto_whenCreateUser_thenNormalizesEmail_hashesPassword_setsUserRole_andReturnsUserCreateDto() {
        UserCreateDto userCreateDto = new UserCreateDto("Wojciech", "Andrzejczak", "wojciechandrzejczak@gmail.com", "password");
        when(passwordEncoder.encode(userCreateDto.password())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User user = inv.getArgument(0);
            user.setId(42L);
            return user;
        });

        User saved = userService.createUser(userCreateDto);

        assertThat(saved.getId()).isEqualTo(42L);
        assertThat(saved.getEmail()).isEqualTo("wojciechandrzejczak@gmail.com");
        assertThat(saved.getPassword()).isEqualTo("hashed");
        assertThat(saved.getRoles()).containsExactly(Role.USER);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User toPersist = captor.getValue();

        assertThat(toPersist.getEmail()).isEqualTo("wojciechandrzejczak@gmail.com");
        assertThat(toPersist.getPassword()).isEqualTo("hashed");
        assertThat(toPersist.getRoles()).containsExactly(Role.USER);

        verify(passwordEncoder).encode("password");
    }
}
