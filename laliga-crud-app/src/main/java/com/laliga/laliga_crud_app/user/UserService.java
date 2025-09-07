package com.laliga.laliga_crud_app.user;

import com.laliga.laliga_crud_app.user.dto.UserCreateDto;
import com.laliga.laliga_crud_app.user.dto.UserReadDto;
import com.laliga.laliga_crud_app.user.dto.UserUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.laliga.laliga_crud_app.user.UserMapper.toDTO;
import static com.laliga.laliga_crud_app.user.UserMapper.toNewEntity;

@Transactional(readOnly = true)
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public List<UserReadDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(norm(email)).orElseThrow(EntityNotFoundException::new);
    }

    public UserReadDto findDTOByEmail(String email) {
        return toDTO(userRepository.findByEmail(norm(email)).orElseThrow(EntityNotFoundException::new));
    }
    @Transactional
    public User createUser(UserCreateDto userCreateDTO) {
        User user = toNewEntity(userCreateDTO);
        user.setEmail(norm(userCreateDTO.email()));
        user.setPassword(passwordEncoder.encode(userCreateDTO.password()));
        user.setRoles(EnumSet.of(Role.USER));
        userRepository.save(user);
        return user;
    }

    @Transactional
    public UserReadDto updateUser(String email, UserUpdateDto userUpdateDTO) {
        User user = userRepository.findByEmail(norm(email)).orElseThrow(EntityNotFoundException::new);

        if (userUpdateDTO.firstname() != null && !userUpdateDTO.firstname().isBlank()) {
            user.setFirstname(userUpdateDTO.firstname());
        }
        if (userUpdateDTO.lastname() != null && !userUpdateDTO.lastname().isBlank()) {
            user.setLastname(userUpdateDTO.lastname());
        }
        if (userUpdateDTO.password() != null && !userUpdateDTO.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.password()));
        }

        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    @Transactional
    public void deleteByEmail(String email) {
        if (userRepository.findByEmail(norm(email)).isPresent()) {
            userRepository.deleteByEmail(norm(email));
        } else {
            throw new EntityNotFoundException();
        }
    }

    private static String norm(String email) {return email.trim().toLowerCase();}
}
