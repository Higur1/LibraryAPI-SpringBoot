package io.github.spring.libraryapi.service;

import io.github.spring.libraryapi.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final AuthUserService authUserService;

    public AuthUser getUserLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authUserService.getByLogin(((UserDetails) authentication.getPrincipal()).getUsername());
    }
}
