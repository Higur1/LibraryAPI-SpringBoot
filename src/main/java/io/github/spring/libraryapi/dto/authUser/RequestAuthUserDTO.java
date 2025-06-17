package io.github.spring.libraryapi.dto.authUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record RequestAuthUserDTO(
        @NotBlank(message = "required field")
        String login,
        @NotBlank(message = "required field")
        String password,
        @NotBlank(message = "required field")
        @Email(message = "email invalid")
        String email,
        List<String> roles) {
}
