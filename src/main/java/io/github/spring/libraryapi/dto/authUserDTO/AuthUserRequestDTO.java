package io.github.spring.libraryapi.dto.authUserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(name = "AuthUserRequest")
public record AuthUserRequestDTO(
        @NotBlank(message = "required field")
        String login,
        @NotBlank(message = "required field")
        String password,
        @NotBlank(message = "required field")
        @Email(message = "email invalid")
        String email,
        List<String> roles) {
}
