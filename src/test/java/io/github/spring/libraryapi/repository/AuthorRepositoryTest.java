package io.github.spring.libraryapi.repository;

import io.github.spring.libraryapi.model.Author;
import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.model.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ActiveProfiles("test")
@Sql(scripts = "/test-setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AuthorRepositoryTest {
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    private UUID ancientAuthorId;

    @BeforeEach
    void setup(){
        Author ancientAuthor  = new Author();
        ancientAuthor.setId(UUID.randomUUID());
        ancientAuthor.setName("Ancient Author");
        ancientAuthor.setNationality("Brazilian");
        ancientAuthor.setBirthDate(LocalDate.of(1905,1,1));
        Author ancientAuthorSave = authorRepository.save(ancientAuthor);
        ancientAuthorId = ancientAuthorSave.getId();
    }

    @Test
    public void saveTest(){
        Author author = authorRepository.findById(ancientAuthorId).get();

        assertNotNull(author);
        assertEquals("Ancient Author", author.getName());
    }

    @Test
    public void listAuthorsTest(){
        List<Author> listAuthors = authorRepository.findAll();

        assertFalse(listAuthors.isEmpty());
    }

    @Test
    public void updateTest(){
        Optional<Author> author = authorRepository.findById(ancientAuthorId);
        author.get().setName("New Author");
        Author saveAuthor = authorRepository.save(author.get());

        assertNotNull(saveAuthor);
        assertEquals("New Author", saveAuthor.getName());
    }

    @Test
    public void deleteTest(){
        authorRepository.deleteById(ancientAuthorId);

        Optional<Author> byId = authorRepository.findById(ancientAuthorId);

        assertFalse(byId.isPresent());
    }

    @Test
    public void saveWithBooksTest(){
        Author author = new Author();
        author.setName("Pedro");
        author.setNationality("Brazilian");
        author.setBirthDate(LocalDate.of(1970, 8, 5 ));

        Book book = new Book();
        book.setIsbn("484684684-46846");
        book.setPrice(BigDecimal.valueOf(100));
        book.setGenre(Genre.FANTASY);
        book.setTitle("Another book");
        book.setReleaseDate(LocalDate.of(1980,1,2));
        book.setAuthor(author);

        author.setBooks(new ArrayList<>());
        author.getBooks().add(book);

        Author authorSave = authorRepository.save(author);
        List<Book> booksSave = bookRepository.saveAll(author.getBooks());

        assertNotNull(authorSave);
        assertFalse(booksSave.isEmpty());

        assertEquals("Pedro", authorSave.getName());
        assertTrue(booksSave.contains(book));
    }
}
