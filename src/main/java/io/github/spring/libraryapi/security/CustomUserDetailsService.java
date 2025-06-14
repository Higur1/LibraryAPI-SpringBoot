package io.github.spring.libraryapi.security;

import io.github.spring.libraryapi.model.AuthUser;
import io.github.spring.libraryapi.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserService service;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        AuthUser user = service.getByLogin(login);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[user.getRoles().size()]))
                .build();
    }
}
