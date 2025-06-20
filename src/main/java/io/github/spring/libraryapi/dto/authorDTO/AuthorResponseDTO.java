package io.github.spring.libraryapi.dto.authorDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "AuthorResponse")
public record AuthorResponseDTO(
        @Schema(name = "id")
        UUID id,
        @Schema(name = "name")
        String name,
        @Schema(name = "birthDate")
        LocalDate birthDate,
        @Schema(name = "nationality")
        String nationality,
        @Schema(name = "authUsername")
        String authUsername) {
}
