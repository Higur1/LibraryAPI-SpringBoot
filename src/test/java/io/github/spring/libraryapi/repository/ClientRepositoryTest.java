package io.github.spring.libraryapi.repository;

import io.github.spring.libraryapi.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(scripts = "/test-setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ClientRepositoryTest {
    @Autowired
    ClientRepository clientRepository;

    private UUID clientUUID;

    @BeforeEach
    void setup() {
        Client client = new Client();
        client.setClientId("client-test");
        client.setClientSecret("password-secret-test");
        client.setRedirectURI("http://localhost:8080/test");
        client.setScope("MANAGER");

        Client saveClient = clientRepository.save(client);
        clientUUID = saveClient.getId();
    }

    @Test
    void saveTest() {
        Optional<Client> findClient = clientRepository.findById(clientUUID);

        assertNotNull(findClient);
        assertEquals("client-test", findClient.get().getClientId());
        assertEquals("http://localhost:8080/test", findClient.get().getRedirectURI());
        assertEquals("MANAGER", findClient.get().getScope());
    }

    @Test
    void listClientsTest() {
        List<Client> listClient = clientRepository.findAll();

        assertFalse(listClient.isEmpty());
    }

    @Test
    void updateClientTest() {
        Optional<Client> findClient = clientRepository.findById(clientUUID);

        findClient.get().setClientId("test-test");

        Client saveClient = clientRepository.save(findClient.get());

        assertNotNull(saveClient);
        assertEquals("test-test", saveClient.getClientId());
        assertEquals("MANAGER", saveClient.getScope());
        assertEquals("http://localhost:8080/test", saveClient.getRedirectURI());
    }

    @Test
    void deleteTest() {
        clientRepository.deleteById(clientUUID);

        Optional<Client> findClient = clientRepository.findById(clientUUID);

        assertFalse(findClient.isPresent());
    }
}
