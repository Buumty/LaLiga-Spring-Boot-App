package com.laliga.laliga_crud_app.entities.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        @NotBlank @Size(max = 100) String firstname,
        @NotBlank @Size(max = 100) String lastname,
        @Email @NotBlank @Size(max = 255) String email,
        @NotBlank @Size (min = 8, max = 72) String password
) {
}
