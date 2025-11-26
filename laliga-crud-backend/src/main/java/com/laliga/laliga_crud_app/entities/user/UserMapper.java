package com.laliga.laliga_crud_app.entities.user;

import com.laliga.laliga_crud_app.entities.user.dto.UserCreateDto;
import com.laliga.laliga_crud_app.entities.user.dto.UserReadDto;
import org.springframework.lang.NonNull;

import java.util.Set;

public final class UserMapper {
    public static UserReadDto toDto(@NonNull User user) {
        return new UserReadDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                Set.copyOf(user.getRoles())
        );
    }
    public static User toNewEntity(@NonNull UserCreateDto userCreateDTO) {
        User user = new User();
        user.setFirstname(userCreateDTO.firstname());
        user.setLastname(userCreateDTO.lastname());
        user.setEmail(userCreateDTO.email());
        user.setPassword(userCreateDTO.password());

        return user;
    }
}
