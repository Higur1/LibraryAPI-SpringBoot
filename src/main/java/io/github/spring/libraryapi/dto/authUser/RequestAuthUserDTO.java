package io.github.spring.libraryapi.dto.authUser;

import java.util.List;

public record RequestAuthUserDTO(String login, String password, List<String> roles) {
}
