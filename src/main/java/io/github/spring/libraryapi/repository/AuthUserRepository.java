package io.github.spring.libraryapi.repository;

import io.github.spring.libraryapi.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    AuthUser findByLogin(String login);

    AuthUser findByEmail(String email);
}
