package io.github.spring.libraryapi.repository;

import io.github.spring.libraryapi.model.Author;
import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@SpringBootTest
public class AuthorRepositoryTest {
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void saveTest(){
        Author author = new Author();

        author.setName("Higor");
        author.setNationality("Brazilian");
        author.setBirthDate(LocalDate.of(1999,1,1));

        Author saveAuthor = authorRepository.save(author);

        System.out.println("Author: "+saveAuthor);
    }
    @Test
    public void updateTest(){
        var id = UUID.fromString("c073af6e-407c-43ad-8565-bbfccb456d48");
        Optional<Author> author = authorRepository.findById(id);

        if(author.isPresent()){
            //change
            Author foundAuthor = author.get();
            foundAuthor.setBirthDate(LocalDate.of(1800, 11, 13));

            var updateAuthor = authorRepository.save(foundAuthor);
            System.out.println("Updated: "+updateAuthor);
        }
    }
    @Test
    public void listAuthorsTest(){
        List<Author> listAuthors = authorRepository.findAll();
        listAuthors.forEach(System.out::println);
    }

    @Test
    public void countAuthorsTest(){
        System.out.printf("Database has %s authors", authorRepository.count());
    }

    @Test
    public void deleteTest(){
        var id = UUID.fromString("c073af6e-407c-43ad-8565-bbfccb456d48");
        Optional<Author> author = authorRepository.findById(id);

        author.ifPresent(value -> authorRepository.deleteById(value.getId()));
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

        authorRepository.save(author);
        bookRepository.saveAll(author.getBooks());
    }

    @Test
    void listBooksByAuthor(){
        UUID uuid = UUID.fromString("2f5e6112-dbc9-46ee-9975-2bc7ce1c95c8");
        Author author = authorRepository.findById(uuid).get();

        List<Book> byAuthor = bookRepository.findByAuthor(author);

        byAuthor.forEach(System.out::println);
    }
}
