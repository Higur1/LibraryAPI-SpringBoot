package io.github.spring.libraryapi.dto.authUserDTO;

import java.util.List;
import java.util.UUID;

public record ResponseAuthUserDTO(UUID id, String login, String email, List<String> roles) {
}
