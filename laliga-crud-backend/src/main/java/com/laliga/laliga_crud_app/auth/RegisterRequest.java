package com.laliga.laliga_crud_app.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private @NotBlank @Size(max = 100) String firstname;
    private @NotBlank @Size(max = 100) String lastname;
    private @Email @NotBlank @Size(max = 255) String email;
    private @NotBlank @Size(min = 8, max = 72) String password;
}
