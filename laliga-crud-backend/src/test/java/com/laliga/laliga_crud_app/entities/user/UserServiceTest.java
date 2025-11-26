package com.laliga.laliga_crud_app.entities.user;

import com.laliga.laliga_crud_app.entities.user.dto.UserCreateDto;
import com.laliga.laliga_crud_app.entities.user.dto.UserReadDto;
import com.laliga.laliga_crud_app.entities.user.dto.UserUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // === findAllUsersDto ===

    @Test
    void givenUsersInRepository_whenFindAllUsersDto_thenReturnMappedDtos() {
        // given
        User u1 = new User();
        u1.setId(1L);
        u1.setEmail("user1@example.com");
        User u2 = new User();
        u2.setId(2L);
        u2.setEmail("user2@example.com");

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        UserReadDto dto1 = new UserReadDto(
                1L,
                "John",
                "Doe",
                "user1@example.com",
                EnumSet.of(Role.USER)
        );
        UserReadDto dto2 = new UserReadDto(
                2L,
                "Jane",
                "Doe",
                "user2@example.com",
                EnumSet.of(Role.ADMIN)
        );

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            mocked.when(() -> UserMapper.toDto(u1)).thenReturn(dto1);
            mocked.when(() -> UserMapper.toDto(u2)).thenReturn(dto2);

            // when
            List<UserReadDto> result = userService.findAllUsersDto();

            // then
            assertThat(result).containsExactly(dto1, dto2);

            verify(userRepository).findAll();

            mocked.verify(() -> UserMapper.toDto(u1));
            mocked.verify(() -> UserMapper.toDto(u2));
            mocked.verifyNoMoreInteractions();
        }
    }

    // === findUserByEmail ===

    @Test
    void givenExistingUser_whenFindUserByEmail_thenReturnUserWithNormalizedEmail() {
        // given
        String rawEmail = "  TEST@MAIL.COM  ";
        String normEmail = "test@mail.com";

        User user = new User();
        user.setEmail(normEmail);

        when(userRepository.findByEmail(normEmail)).thenReturn(Optional.of(user));

        // when
        User result = userService.findUserByEmail(rawEmail);

        // then
        assertThat(result).isSameAs(user);

        verify(userRepository).findByEmail(normEmail);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void givenNonExistingUser_whenFindUserByEmail_thenThrowEntityNotFound() {
        // given
        String rawEmail = "  notfound@mail.com  ";
        String normEmail = "notfound@mail.com";

        when(userRepository.findByEmail(normEmail)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> userService.findUserByEmail(rawEmail))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository).findByEmail(normEmail);
        verifyNoMoreInteractions(userRepository);
    }

    // === createUser ===

    @Test
    void givenUserCreateDto_whenCreateUser_thenEncodePasswordSetRoleAndSave() {
        // given
        UserCreateDto createDto = new UserCreateDto(
                "John",
                "Doe",
                "  NEW@MAIL.COM  ",
                "rawPassword"
        );

        String normEmail = "new@mail.com";

        User newUserEntity = new User(); // tworzony przez mapper
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            mocked.when(() -> UserMapper.toNewEntity(createDto)).thenReturn(newUserEntity);

            // żeby save zwracał to, co dostanie
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // when
            User result = userService.createUser(createDto);

            // then
            assertThat(result).isSameAs(newUserEntity);
            assertThat(result.getEmail()).isEqualTo(normEmail);
            assertThat(result.getPassword()).isEqualTo("encodedPassword");
            assertThat(result.getRoles()).isEqualTo(EnumSet.of(Role.USER));

            mocked.verify(() -> UserMapper.toNewEntity(createDto));

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            User saved = userCaptor.getValue();

            assertThat(saved).isSameAs(newUserEntity);
            assertThat(saved.getEmail()).isEqualTo(normEmail);
            assertThat(saved.getPassword()).isEqualTo("encodedPassword");
            assertThat(saved.getRoles()).isEqualTo(EnumSet.of(Role.USER));

            verify(passwordEncoder).encode("rawPassword");
            verifyNoMoreInteractions(userRepository, passwordEncoder);
        }
    }

    // === deleteByEmail ===

    @Test
    void givenExistingUser_whenDeleteByEmail_thenDeleteIsCalledWithNormalizedEmail() {
        // given
        String rawEmail = "  USER@MAIL.COM ";
        String normEmail = "user@mail.com";

        when(userRepository.existsByEmail(rawEmail)).thenReturn(true);

        // when
        userService.deleteByEmail(rawEmail);

        // then
        verify(userRepository).existsByEmail(rawEmail);
        verify(userRepository).deleteByEmail(normEmail);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void givenNonExistingUser_whenDeleteByEmail_thenThrowEntityNotFoundAndDoNotDelete() {
        // given
        String rawEmail = "  UNKNOWN@MAIL.COM ";
        when(userRepository.existsByEmail(rawEmail)).thenReturn(false);

        // when + then
        assertThatThrownBy(() -> userService.deleteByEmail(rawEmail))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository).existsByEmail(rawEmail);
        verify(userRepository, never()).deleteByEmail(anyString());
        verifyNoMoreInteractions(userRepository);
    }

    // === findUserDtoByEmail ===

    @Test
    void givenExistingUser_whenFindUserDtoByEmail_thenReturnMappedDto() {
        // given
        String rawEmail = "  USER@MAIL.COM ";
        String normEmail = "user@mail.com";

        User user = new User();
        user.setEmail(normEmail);

        when(userRepository.findByEmail(normEmail)).thenReturn(Optional.of(user));

        UserReadDto dto = new UserReadDto(
                1L,
                "John",
                "Doe",
                normEmail,
                EnumSet.of(Role.USER)
        );

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            mocked.when(() -> UserMapper.toDto(user)).thenReturn(dto);

            // when
            UserReadDto result = userService.findUserDtoByEmail(rawEmail);

            // then
            assertThat(result).isEqualTo(dto);

            verify(userRepository).findByEmail(normEmail);
            verifyNoMoreInteractions(userRepository);

            mocked.verify(() -> UserMapper.toDto(user));
            mocked.verifyNoMoreInteractions();
        }
    }

    @Test
    void givenNonExistingUser_whenFindUserDtoByEmail_thenThrowEntityNotFound() {
        // given
        String rawEmail = "  UNKNOWN@MAIL.COM ";
        String normEmail = "unknown@mail.com";

        when(userRepository.findByEmail(normEmail)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> userService.findUserDtoByEmail(rawEmail))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository).findByEmail(normEmail);
        verifyNoMoreInteractions(userRepository);
    }

    // === updateUser ===

    @Test
    void givenExistingUserAndValidUpdateData_whenUpdateUser_thenUpdateFieldsEncodePasswordAndReturnDto() {
        // given
        String rawEmail = "  USER@MAIL.COM ";
        String normEmail = "user@mail.com";

        User user = new User();
        user.setEmail(normEmail);
        user.setFirstname("Old");
        user.setLastname("Name");
        user.setPassword("oldPass");

        UserUpdateDto updateDto = new UserUpdateDto(
                "  NewFirst  ",
                " NewLast ",
                "newPassword"
        );

        when(userRepository.findByEmail(normEmail)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        User savedUser = user; // zakładamy, że save zwraca ten sam obiekt
        when(userRepository.save(user)).thenReturn(savedUser);

        UserReadDto dto = new UserReadDto(
                1L,
                "NewFirst",
                "NewLast",
                normEmail,
                EnumSet.of(Role.USER)
        );

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            mocked.when(() -> UserMapper.toDto(savedUser)).thenReturn(dto);

            // when
            UserReadDto result = userService.updateUser(rawEmail, updateDto);

            // then
            assertThat(result).isEqualTo(dto);

            assertThat(user.getFirstname()).isEqualTo("NewFirst");
            assertThat(user.getLastname()).isEqualTo("NewLast");
            assertThat(user.getPassword()).isEqualTo("encodedNewPassword");

            verify(userRepository).findByEmail(normEmail);
            verify(userRepository).save(user);
            verifyNoMoreInteractions(userRepository);

            verify(passwordEncoder).encode("newPassword");
            verifyNoMoreInteractions(passwordEncoder);

            mocked.verify(() -> UserMapper.toDto(savedUser));
            mocked.verifyNoMoreInteractions();
        }
    }

    @Test
    void givenExistingUserAndBlankFields_whenUpdateUser_thenIgnoreBlankAndDoNotEncodePassword() {
        // given
        String rawEmail = "  USER@MAIL.COM ";
        String normEmail = "user@mail.com";

        User user = new User();
        user.setEmail(normEmail);
        user.setFirstname("OldFirst");
        user.setLastname("OldLast");
        user.setPassword("oldPass");

        UserUpdateDto updateDto = new UserUpdateDto(
                "   ",   // blank
                null,   // null
                "   "   // blank password
        );

        when(userRepository.findByEmail(normEmail)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserReadDto dto = new UserReadDto(
                1L,
                "OldFirst",
                "OldLast",
                normEmail,
                EnumSet.of(Role.USER)
        );

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            mocked.when(() -> UserMapper.toDto(user)).thenReturn(dto);

            // when
            UserReadDto result = userService.updateUser(rawEmail, updateDto);

            // then
            assertThat(result).isEqualTo(dto);

            assertThat(user.getFirstname()).isEqualTo("OldFirst");
            assertThat(user.getLastname()).isEqualTo("OldLast");
            assertThat(user.getPassword()).isEqualTo("oldPass");

            verify(userRepository).findByEmail(normEmail);
            verify(userRepository).save(user);
            verifyNoMoreInteractions(userRepository);

            verify(passwordEncoder, never()).encode(anyString());

            mocked.verify(() -> UserMapper.toDto(user));
            mocked.verifyNoMoreInteractions();
        }
    }

    @Test
    void givenNonExistingUser_whenUpdateUser_thenThrowEntityNotFound() {
        // given
        String rawEmail = "  UNKNOWN@MAIL.COM ";
        String normEmail = "unknown@mail.com";

        UserUpdateDto updateDto = new UserUpdateDto(
                "NewFirst",
                "NewLast",
                "newPassword"
        );

        when(userRepository.findByEmail(normEmail)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> userService.updateUser(rawEmail, updateDto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository).findByEmail(normEmail);
        verify(userRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }
}
