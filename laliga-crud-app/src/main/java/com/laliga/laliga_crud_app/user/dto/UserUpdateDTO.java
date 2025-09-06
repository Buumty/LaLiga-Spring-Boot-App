package com.laliga.laliga_crud_app.user.dto;

import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @Size(max = 100) String firstname,
        @Size(max = 100) String lastname,
        @Size(min = 8, max = 72) String password
) {}
