package io.github.spring.libraryapi.dto.bookDTO;

import io.github.spring.libraryapi.dto.authorDTO.ResponseAuthorDTO;
import io.github.spring.libraryapi.model.Author;
import io.github.spring.libraryapi.model.Genre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ResponseBookDTO(
        UUID id,
        String isbn,
        String title,
        LocalDate releaseDate,
        Genre genre,
        BigDecimal price,
        ResponseAuthorDTO author) {
}
