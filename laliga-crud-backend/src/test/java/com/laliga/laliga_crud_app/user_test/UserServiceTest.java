package com.laliga.laliga_crud_app.user_test;

import com.laliga.laliga_crud_app.user.Role;
import com.laliga.laliga_crud_app.user.User;
import com.laliga.laliga_crud_app.user.UserRepository;
import com.laliga.laliga_crud_app.user.UserService;
import com.laliga.laliga_crud_app.user.dto.UserCreateDto;
import com.laliga.laliga_crud_app.user.dto.UserReadDto;
import com.laliga.laliga_crud_app.user.dto.UserUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
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
        UserCreateDto userCreateDto = new UserCreateDto("A", "B", "email@gmail.com", "password");
        when(passwordEncoder.encode(userCreateDto.password())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User user = inv.getArgument(0);
            user.setId(42L);
            return user;
        });

        User saved = userService.createUser(userCreateDto);

        assertThat(saved.getId()).isEqualTo(42L);
        assertThat(saved.getEmail()).isEqualTo("email@gmail.com");
        assertThat(saved.getPassword()).isEqualTo("hashed");
        assertThat(saved.getRoles()).containsExactly(Role.USER);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User toPersist = captor.getValue();

        assertThat(toPersist.getEmail()).isEqualTo("email@gmail.com");
        assertThat(toPersist.getPassword()).isEqualTo("hashed");
        assertThat(toPersist.getRoles()).containsExactly(Role.USER);

        verify(passwordEncoder).encode("password");
    }

    @Test
    void givenUser_whenUpdateUser_thenUpdatesOnlyProvidedFields_andHashesPasswordWhenPresent() {
        var existing = new User();
        existing.setEmail("email@gmail.com");
        existing.setFirstname("A");
        existing.setLastname("B");
        existing.setPassword("oldpassword");
        existing.setRoles(EnumSet.of(Role.USER));

        when(userRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newpassword")).thenReturn("new-hash");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = new UserUpdateDto(" NewName ", null, "newpassword");

        UserReadDto out = userService.updateUser("email@gmail.com", dto);

        assertThat(out.firstname()).isEqualTo("NewName");
        assertThat(out.lastname()).isEqualTo("B");
        assertThat(existing.getPassword()).isEqualTo("new-hash");
        verify(passwordEncoder,times(1)).encode("newpassword");
        verify(userRepository,times(1)).findByEmail("email@gmail.com");
        verify(userRepository,times(1)).save(existing);

    }

    @Test
    void givenWrongUser_whenUpdateUser_thenThrows404() {
        when(userRepository.findByEmail("wrong@email.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.
                updateUser("wrong@email.com", new UserUpdateDto(null,null,null)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void givenCorrectEmail_whenFindDtoByEmail_thenReturnDto() {
        var user = new User();
        user.setId(42L);
        user.setEmail("email@gmail.com");
        user.setFirstname("A");
        user.setLastname("B");
        user.setRoles(EnumSet.of(Role.USER));

        when(userRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(user));

        UserReadDto userDtoByEmail = userService.findUserDtoByEmail("  email@gmail.com  ");

        assertThat(userDtoByEmail.id()).isEqualTo(42L);
        assertThat(userDtoByEmail.email()).isEqualTo("email@gmail.com");
        verify(userRepository, times(1)).findByEmail("email@gmail.com");
    }

    @Test
    void givenWrongEmail_whenFindDtoByEmail_thenReturn404() {
        when(userRepository.findByEmail("wrong@gmail.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findUserDtoByEmail("wrong@gmail.com"))
                .isInstanceOf(EntityNotFoundException.class);
    }
    @Test
    void deleteByEmail_deletes_whenUserExists_andNormalizesEmail() {
        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(new User()));

        userService.deleteByEmail("  USER@example.COM  ");

        verify(userRepository).findByEmail("user@example.com");
        verify(userRepository).deleteByEmail("user@example.com");
        verifyNoMoreInteractions(userRepository);
    }
    @Test
    void deleteByEmail_throws404_whenUserMissing() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteByEmail("missing@example.com"))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository).findByEmail("missing@example.com");
        verify(userRepository, never()).deleteByEmail(anyString());
    }
}
