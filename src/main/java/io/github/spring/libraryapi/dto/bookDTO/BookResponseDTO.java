package io.github.spring.libraryapi.dto.bookDTO;

import io.github.spring.libraryapi.dto.authorDTO.AuthorResponseDTO;
import io.github.spring.libraryapi.model.Genre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BookResponseDTO(
        UUID id,
        String isbn,
        String title,
        LocalDate releaseDate,
        Genre genre,
        BigDecimal price,
        AuthorResponseDTO author,
        String authUsername
) {
}
