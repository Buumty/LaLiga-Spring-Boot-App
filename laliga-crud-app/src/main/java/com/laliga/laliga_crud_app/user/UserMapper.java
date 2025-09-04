package com.laliga.laliga_crud_app.user;

import com.laliga.laliga_crud_app.user.dto.UserCreateDTO;
import com.laliga.laliga_crud_app.user.dto.UserReadDTO;
import org.springframework.lang.NonNull;

import java.util.Set;

public final class UserMapper {
    public static UserReadDTO toDTO(@NonNull User user) {
        return new UserReadDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                Set.copyOf(user.getRoles())
        );
    }
    public static User toNewEntity(@NonNull UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setFirstname(userCreateDTO.firstname());
        user.setLastname(userCreateDTO.lastname());
        user.setEmail(userCreateDTO.email());
        user.setPassword(userCreateDTO.password());

        return user;
    }
}
