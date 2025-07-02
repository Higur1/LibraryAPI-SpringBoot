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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ActiveProfiles("test")
@Sql(scripts = "/test-setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;


    private UUID bookId;
    private UUID oldAuthorId;
    private UUID newAuthorId;
    private UUID anotherBookId;

    @BeforeEach
    void setup() {
        Author ancientAuthor = new Author();
        ancientAuthor.setId(UUID.randomUUID());
        ancientAuthor.setBirthDate(LocalDate.of(1908, 6, 27));
        ancientAuthor.setNationality("Brazilian");
        ancientAuthor.setName("Ancient Author");
        Author ancientAuthorResult = authorRepository.save(ancientAuthor);
        oldAuthorId = ancientAuthorResult.getId();

        Author newAuthor = new Author();
        newAuthor.setId(UUID.randomUUID());
        newAuthor.setBirthDate(LocalDate.of(1908, 6, 27));
        newAuthor.setNationality("Brazilian");
        newAuthor.setName("New Author");
        Author newAuthorResult = authorRepository.save(newAuthor);
        newAuthorId = newAuthorResult.getId();

        Book book = new Book();
        book.setIsbn("978-8535931983");
        book.setGenre(Genre.FICTION);
        book.setPrice(BigDecimal.valueOf(100));
        book.setTitle("Test Book");
        book.setReleaseDate(LocalDate.of(2020, 1, 1));
        book.setAuthor(authorRepository.getReferenceById(oldAuthorId));
        Book bookSave = bookRepository.save(book);
        bookId = bookSave.getId();

        Book secondBook = new Book();
        secondBook.setIsbn("978-8535931980");
        secondBook.setGenre(Genre.BIOGRAPHY);
        secondBook.setPrice(BigDecimal.valueOf(100));
        secondBook.setTitle("Test Book 2");
        secondBook.setReleaseDate(LocalDate.of(2019, 1, 1));
        secondBook.setAuthor(authorRepository.getReferenceById(newAuthorId));
        Book secondBookSave = bookRepository.save(secondBook);
        anotherBookId = secondBookSave.getId();
    }

    @Test
        //save without cascade
    void saveTest() {
        Book book = bookRepository.findById(bookId).orElse(null);

        assertNotNull(book);

        assertNotNull(book.getId());
        assertEquals("Test Book", book.getTitle());
        assertEquals("Ancient Author", book.getAuthor().getName());
    }

    @Test
    //is dangerous using cascade
    void saveWithCascadeTest() {
        Author author = new Author();
        author.setId(UUID.randomUUID());
        author.setBirthDate(LocalDate.of(1908, 6, 27));
        author.setNationality("Brazilian");
        author.setName("Guimarães Rosa");
        Author authorSave = authorRepository.save(author);

        Book book = new Book();
        book.setIsbn("978-8535931982");
        book.setGenre(Genre.FICTION);
        book.setPrice(BigDecimal.valueOf(53.20));
        book.setTitle("Grande Sertão Veredas");
        book.setReleaseDate(LocalDate.of(1956, 1, 1));
        book.setAuthor(authorSave);
        bookRepository.save(book);

        assertNotNull(book.getId());
        assertEquals("Grande Sertão Veredas", book.getTitle());
        assertEquals("Guimarães Rosa", book.getAuthor().getName());
    }

    @Test
    void updateAuthorOfTheBookTest() {

        Author author = new Author();
        author.setId(UUID.randomUUID());
        author.setBirthDate(LocalDate.of(1912, 8, 10));
        author.setNationality("Brazilian");
        author.setName("Jorge Amado");

        authorRepository.save(author);

        Book book = bookRepository.findById(bookId).orElse(null);

        book.setAuthor(author);

        Book saveBook = bookRepository.save(book);

        assertNotNull(saveBook);

        assertNotNull(saveBook.getId());

        assertEquals("Jorge Amado", saveBook.getAuthor().getName());
        assertEquals(bookId, saveBook.getId());
    }

    @Test
        //@Transactional use to open transactional with db to many query's
    void findBookTest() {
        Book book = bookRepository.findById(bookId).orElse(null);

        assertNotNull(book);
        assertEquals(bookId, book.getId());
    }

    @Test
    void findByTitleTest() {
        Optional<Book> byId = bookRepository.findById(bookId);
        List<Book> list = bookRepository.findByTitle("Test Book");

        assertEquals(1, list.size());
        assertTrue(list.contains(byId.get()));
    }

    @Test
    void findByIsbnTest() {
        Optional<Book> book = bookRepository.findByIsbn("978-8535931983");

        assertNotNull(book);
        assertEquals("Test Book", book.get().getTitle());
    }

    @Test
    void listBooksWithQueryJPQLTest() {
        List<Book> result = bookRepository.listAllOrderByTitleAndPrice();

        assertFalse(result.isEmpty());

        List<Book> sorted = result
                .stream()
                .sorted(Comparator.comparing(Book::getTitle).thenComparing(Book::getPrice))
                .toList();

        assertEquals(sorted, result);
    }

    @Test
    void listBookAuthorsTest() {
        List<Author> authors = bookRepository.listBookAuthors();

        assertFalse(authors.isEmpty());
    }

    @Test
    void listGenreOfBrazilianAuthorsTest() {
        List<String> genres = bookRepository.listOfGenreBrazilianAuthors();

        assertFalse(genres.isEmpty());
    }

    @Test
    void listByGenreOrderByPriceWithQueryParamTest() {
        List<Book> byGenreOrderPrice = bookRepository.findByGenreOrderPrice(Genre.FICTION, BigDecimal.valueOf(200));

        assertNotNull(byGenreOrderPrice);

        byGenreOrderPrice.forEach(book -> {
            assertEquals(Genre.FICTION, book.getGenre());
        });
    }

    @Test
    void findByDistinctNameTest() {
        List<String> strings = bookRepository.listOfDistinctName();

        assertNotNull(strings);

        assertTrue(strings.contains("Test Book"));
    }

    @Test
    void updateReleaseDateTest() {
        LocalDate newDate = LocalDate.of(2000,1,1);
        bookRepository.updateReleaseDate(newDate, bookId);

        Book byId = bookRepository.findById(bookId).orElseThrow(AssertionError::new);

        assertEquals(newDate, byId.getReleaseDate());
    }

    @Test
    void deleteTest() {
        bookRepository.deleteById(bookId);

        Optional<Book> byId = bookRepository.findById(bookId);

        assertTrue(byId.isEmpty());
    }

    @Test
    void deleteByGenreTest() {
        bookRepository.deleteByGenre(Genre.FICTION);

        List<Book> all = bookRepository.findAll();

        all.forEach(book -> {
            assertNotEquals(Genre.FICTION, book.getGenre());
        });
    }
}
