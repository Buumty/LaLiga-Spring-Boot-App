package com.laliga.laliga_crud_app.user;

import com.laliga.laliga_crud_app.user.dto.UserCreateDTO;
import com.laliga.laliga_crud_app.user.dto.UserReadDTO;
import com.laliga.laliga_crud_app.user.dto.UserUpdateDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.laliga.laliga_crud_app.user.UserMapper.toDTO;
import static com.laliga.laliga_crud_app.user.UserMapper.toNewEntity;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public List<UserReadDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    public UserReadDTO findDTOByEmail(String email) {
        return toDTO(userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new));
    }
    @Transactional
    public User createUser(UserCreateDTO userCreateDTO) {
        User user = toNewEntity(userCreateDTO);
        user.setEmail(userCreateDTO.email().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(userCreateDTO.password()));
        user.setRoles(EnumSet.of(Role.USER));
        userRepository.save(user);
        return user;
    }

    @Transactional
    public UserReadDTO updateUser(String email, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

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
        if (userRepository.findByEmail(email).isPresent()) {
            userRepository.deleteByEmail(email);
        } else {
            throw new EntityNotFoundException();
        }
    }
}
