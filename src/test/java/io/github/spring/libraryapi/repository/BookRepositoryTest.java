package io.github.spring.libraryapi.repository;

import io.github.spring.libraryapi.model.Author;
import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Test
        //save without cascade
    void saveTest() {
        Book book = new Book();

        book.setIsbn("4811981-841");
        book.setGenre(Genre.BIOGRAPHY);
        book.setPrice(BigDecimal.valueOf(100));
        book.setTitle("UFO");
        book.setReleaseDate(LocalDate.of(1980, 1, 1));

      /*
        book.setAuthor(
                authorRepository.findById(
                        UUID.fromString("f57052fb-e139-4d52-b07f-21f32ea0880b"))
                        .orElse(null));*/
        Author author = authorRepository.findById(UUID.fromString("f57052fb-e139-4d52-b07f-21f32ea0880b")).orElse(null);

        book.setAuthor(author);

        bookRepository.save(book);
    }

    @Test
        //is dangerous using cascade
    @Transactional
    void saveWithCascadeTest() {
        Book book = new Book();

        book.setIsbn("4811981-841");
        book.setGenre(Genre.BIOGRAPHY);
        book.setPrice(BigDecimal.valueOf(100));
        book.setTitle("UFO");
        book.setReleaseDate(LocalDate.of(1980, 1, 1));

        Author author = new Author();

        author.setName("TesteHigor");
        author.setNationality("Brazilian");
        author.setBirthDate(LocalDate.of(1999, 1, 1));


        book.setAuthor(author);

        bookRepository.save(book);
    }

    @Test
    void updateAuthorOfTheBook() {
        UUID id = UUID.fromString("d392695b-cc3f-4a4e-984d-5a56104e7da4");
        var oldBook = bookRepository.findById(id).orElse(null);

        Author author = authorRepository.findById(UUID.fromString("d392695b-cc3f-4a4e-984d-5a56104e7da4")).orElse(null);

        oldBook.setAuthor(author);

        bookRepository.save(oldBook);
    }

    @Test
        //@Transactional use to open transactional with db to many query's
    void findBookTest() {
        UUID id = UUID.fromString("d392695b-cc3f-4a4e-984d-5a56104e7da4");
        Book book = bookRepository.findById(id).orElse(null);

        System.out.println("Book: " + book);
    }

    @Test
    void findByTitleTest() {
        List<Book> list = bookRepository.findByTitle("Test");
        list.forEach(System.out::println);
    }

    @Test
    void findByIsbnTest() {
       Optional<Book> book = bookRepository.findByIsbn("978-1585424337");
       book.ifPresent(System.out::println);
    }

    @Test
    void listBooksWithQueryJPQL() {
        var result = bookRepository.listAllOrderByTitleAndPrice();
        result.forEach(System.out::println);
    }

    @Test
    void listBookAuthors() {
        var result = bookRepository.listBookAuthors();
        result.forEach(System.out::println);
    }

    @Test
    void listGenreOfBrazilianAuthors() {
        var result = bookRepository.listOfGenreBrazilianAuthors();
        result.forEach(System.out::println);
    }

    @Test
    void listByGenreOrderByPriceWithQueryParam() {
        var result = bookRepository.findByGenre(Genre.FICTION, BigDecimal.valueOf(200));
        result.forEach(System.out::println);
    }

    @Test
    void deleteByGenreTest(){
        bookRepository.deleteByGenre(Genre.BIOGRAPHY);
    }
    @Test
    void updateReleaseDateTest(){
        bookRepository.updateReleaseDate(LocalDate.of(2000,1,1));
    }
}
