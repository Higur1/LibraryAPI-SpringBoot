package io.github.spring.libraryapi.security;

import io.github.spring.libraryapi.model.AuthUser;
import io.github.spring.libraryapi.service.AuthUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SocialLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String DEFAULT_PASSWORD = System.getenv("GOOGLE_SECRET_KEY");
    private final AuthUserService service;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        AuthUser userByEmail = service.getByEmail(oAuth2User.getAttribute("email"));

        if (userByEmail == null) {
            saveNewAuthUser(oAuth2User);
        }

        authentication = new CustomAuthentication(userByEmail);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        super.onAuthenticationSuccess(
                request,
                response,
                authentication
        );
    }

    private void saveNewAuthUser(OAuth2User user) {
        AuthUser authUser = new AuthUser();
        authUser.setEmail(user.getAttribute("email"));
        authUser.setLogin(handleEmail(Objects.requireNonNull(user.getAttribute("email"))));
        authUser.setPassword(DEFAULT_PASSWORD);
        authUser.setRoles(List.of("OPERATOR"));

        service.save(authUser);
    }

    private String handleEmail(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}
