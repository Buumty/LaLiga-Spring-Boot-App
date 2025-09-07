package com.laliga.laliga_crud_app.user.dto;

import com.laliga.laliga_crud_app.user.Role;

import java.util.Set;

public record UserReadDto(
        Long id,
        String firstname,
        String lastname,
        String email,
        Set<Role> roles
) {}
