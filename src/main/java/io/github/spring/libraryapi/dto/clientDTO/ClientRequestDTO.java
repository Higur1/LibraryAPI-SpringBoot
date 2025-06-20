package io.github.spring.libraryapi.dto.clientDTO;

import jakarta.validation.constraints.NotBlank;

public record ClientRequestDTO(
        @NotBlank(message = "required field")
        String clientId,
        @NotBlank(message = "required field")
        String clientSecret,
        @NotBlank(message = "required field")
        String redirectURI,

        String scope) {
}
