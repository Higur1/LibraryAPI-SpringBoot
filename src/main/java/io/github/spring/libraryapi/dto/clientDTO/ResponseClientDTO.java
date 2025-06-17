package io.github.spring.libraryapi.dto.clientDTO;

import java.util.UUID;

public record ResponseClientDTO(UUID id, String clientId, String clientSecret, String redirectURI, String scope) {
}
