package io.github.spring.libraryapi.dto.clientDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ClientRequest")
public record ClientRequestDTO(
        @NotBlank(message = "required field")
        String clientId,
        @NotBlank(message = "required field")
        String clientSecret,
        @NotBlank(message = "required field")
        String redirectURI,

        String scope) {
}
