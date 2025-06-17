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
        AuthUser userByLogin = service.getByLogin(authentication.getName());

        if (userByLogin == null) {
            throw getUserNotFound();
        }

        boolean matcherPassword =
                encoder.matches(
                        authentication.getCredentials().toString(),
                        userByLogin.getPassword()
                );

        if(matcherPassword){
            return new CustomAuthentication(userByLogin);
        }

        throw getUserNotFound();
    }
    private static UsernameNotFoundException getUserNotFound() {
        return new UsernameNotFoundException("Incorrect username or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }


}
