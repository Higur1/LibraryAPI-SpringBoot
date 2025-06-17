package io.github.spring.libraryapi.security;

import io.github.spring.libraryapi.model.AuthUser;
import io.github.spring.libraryapi.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AuthUserService service;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        AuthUser userByLogin = service.getByLogin(login);

        if (userByLogin == null) {
            throw getUserNotFound();
        }

        String encryptedPassword = userByLogin.getPassword();

        boolean matcherPassword = encoder.matches(password, encryptedPassword);

        if(matcherPassword){
            return new CustomAuthentication(userByLogin);
        }

        throw getUserNotFound();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

    private static UsernameNotFoundException getUserNotFound() {
        return new UsernameNotFoundException("Incorrect username or password");
    }
}
