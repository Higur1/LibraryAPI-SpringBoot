package io.github.spring.libraryapi.dto.authorDTO;

import java.time.LocalDate;
import java.util.UUID;

public record ResponseAuthorDTO(UUID id, String name, LocalDate birthDate, String nationality, String authUsername) {
}
