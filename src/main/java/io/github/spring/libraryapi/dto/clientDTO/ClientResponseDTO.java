package io.github.spring.libraryapi.dto.clientDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "ClientResponse")
public record ClientResponseDTO(
        UUID id,
        String clientId,
        String clientSecret,
        String redirectURI,
        String scope) {
}
