package io.github.spring.libraryapi.dto.bookDTO;

import io.github.spring.libraryapi.model.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BookRequestDTO(
        @NotBlank(message = "Required field")
        @ISBN(message = "Invalid ISBN")
        String isbn,
        @NotBlank(message = "Required field")
        String title,
        @NotNull(message = "Required field")
        @Past(message = "Date must be in the past")
        LocalDate releaseDate,
        Genre genre,
        BigDecimal price,
        @NotNull(message = "Required field")
        UUID authorId
) {
}
