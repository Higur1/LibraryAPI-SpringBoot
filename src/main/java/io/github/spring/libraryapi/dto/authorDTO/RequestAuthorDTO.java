package io.github.spring.libraryapi.dto.authorDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RequestAuthorDTO(
        @NotBlank(message = "Required field")
        @Size(min = 2,max = 100, message = "Name must not exceed 100 characters")
        String name,
        @NotNull
        @Past(message = "Date must be in the past")
        LocalDate birthDate,
        @NotBlank(message = "Required field")
        @Size(min = 3,max = 100, message = "Nationality must not exceed 50 characters")
        String nationality) {
}
