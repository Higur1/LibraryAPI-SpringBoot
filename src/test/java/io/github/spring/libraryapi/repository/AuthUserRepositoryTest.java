package io.github.spring.libraryapi.repository;

import io.github.spring.libraryapi.model.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@Sql(scripts = "/test-setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AuthUserRepositoryTest {

    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    PasswordEncoder encoder;

    private UUID authUserId;

    @BeforeEach
    void setup(){
        AuthUser authUser = new AuthUser();
        authUser.setRoles(List.of("MANAGER", "OPERATOR"));
        authUser.setPassword(encoder.encode("TestPassword"));
        authUser.setLogin("Test Login");
        authUser.setEmail("EmailTest@Test.com");

        AuthUser saveAuthUser = authUserRepository.save(authUser);
        authUserId = saveAuthUser.getId();
        System.out.println(authUser);
        System.out.println(saveAuthUser);
    }
    @Test
    void saveTest(){
        Optional<AuthUser> findAuthUser = authUserRepository.findById(authUserId);

        assertNotNull(findAuthUser.get());
        assertEquals("Test Login", findAuthUser.get().getLogin());
        assertTrue(findAuthUser.get().getRoles().contains("MANAGER"));
    }
    @Test
    void listAuthUsersTest(){
        List<AuthUser> allUsers = authUserRepository.findAll();

        assertFalse(allUsers.isEmpty());
    }
    @Test
    void updateAuthUserTest(){
        Optional<AuthUser> findAuthUser = authUserRepository.findById(authUserId);

        findAuthUser.get().setEmail("Test@Test.com");

        AuthUser saveAuthUser = authUserRepository.save(findAuthUser.get());

        assertNotNull(saveAuthUser);
        assertEquals("Test@Test.com", saveAuthUser.getEmail());
        assertEquals("Test Login", saveAuthUser.getLogin());
    }
    @Test
    void deleteAuthUserTest(){
        authUserRepository.deleteById(authUserId);

        Optional<AuthUser> findAuthUser = authUserRepository.findById(authUserId);

        assertFalse(findAuthUser.isPresent());
    }

    @Test
    void findByLoginTest(){
        AuthUser findAuthUser = authUserRepository.findByLogin("Test Login");

        assertNotNull(findAuthUser);
        assertEquals("EmailTest@Test.com", findAuthUser.getEmail());
        assertEquals("Test Login", findAuthUser.getLogin());
    }

    @Test
    void findByEmailTest(){
        AuthUser findAuthUser = authUserRepository.findByEmail("EmailTest@Test.com");

        assertNotNull(findAuthUser);
        assertEquals("Test Login", findAuthUser.getLogin());
        assertEquals("EmailTest@Test.com", findAuthUser.getEmail());
    }
}
