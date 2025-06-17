package io.github.spring.libraryapi.service;

import io.github.spring.libraryapi.model.Client;
import io.github.spring.libraryapi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final PasswordEncoder encoder;

    public Client save(Client client){
        String passwordEncode = encoder.encode(client.getClientSecret());
        client.setClientSecret(passwordEncode);
        return repository.save(client);
    }

    public Client getByClientId(String clientId){
        return repository.findByClientId(clientId);
    }

}
