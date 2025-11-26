package com.laliga.laliga_crud_app.entities.user.dto;

import jakarta.validation.constraints.Size;

public record UserUpdateDto(
        @Size(max = 100) String firstname,
        @Size(max = 100) String lastname,
        @Size(min = 8, max = 72) String password
) {}
