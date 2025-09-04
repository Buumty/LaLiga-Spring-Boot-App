package com.laliga.laliga_crud_app.user;

import com.laliga.laliga_crud_app.user.dto.UserCreateDTO;
import com.laliga.laliga_crud_app.user.dto.UserReadDTO;
import jakarta.persistence.EntityNotFoundException;
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

    public UserReadDTO findByEmail(String email) {
        return toDTO(userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new));
    }
    public User createUser(UserCreateDTO userCreateDTO) {
        User user = toNewEntity(userCreateDTO);
        user.setEmail(userCreateDTO.email().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(userCreateDTO.password()));
        user.setRoles(EnumSet.of(Role.USER));
        userRepository.save(user);
        return user;
    }
}
