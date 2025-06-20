package io.github.spring.libraryapi.dto.authUserDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(name = "AuthorResponse")
public record AuthUserResponseDTO(UUID id, String login, String email, List<String> roles) {
}
