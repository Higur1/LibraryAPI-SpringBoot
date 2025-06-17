package io.github.spring.libraryapi.security;


import io.github.spring.libraryapi.model.Client;
import io.github.spring.libraryapi.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientService service;

    @Override
    public void save(RegisteredClient registeredClient) {
    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Client byClientId = service.getByClientId(clientId);

        if (byClientId == null) {
            return null;
        }

        return RegisteredClient
                .withId(byClientId.getId().toString())
                .clientId(byClientId.getClientId())
                .clientSecret(byClientId.getClientSecret())
                .redirectUri(byClientId.getRedirectURI())
                .scope(byClientId.getScope())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();
    }
}
