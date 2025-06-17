package io.github.spring.libraryapi.service;

import io.github.spring.libraryapi.model.AuthUser;
import io.github.spring.libraryapi.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository repository;
    private final PasswordEncoder encoder;

    public void save(AuthUser authUser){
        String password = authUser.getPassword();
        authUser.setPassword(encoder.encode(password));

        repository.save(authUser);
    }

    public AuthUser getByLogin(String login){
        return repository.findByLogin(login);
    }

    public AuthUser getByEmail(String email){
        return repository.findByEmail(email);
    }
}
