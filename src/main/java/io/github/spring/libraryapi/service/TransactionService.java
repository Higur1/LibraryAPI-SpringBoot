package io.github.spring.libraryapi.service;

import io.github.spring.libraryapi.model.Author;
import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.model.Genre;
import io.github.spring.libraryapi.repository.AuthorRepository;
import io.github.spring.libraryapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public void updateWithoutUpdate() {
        var book = bookRepository.findById(UUID.fromString("87125a4b-3276-4b60-9ff4-47712d4aac7a")).orElse(null);
        book.setReleaseDate(LocalDate.of(2024, 6, 1));
    }

    @Transactional
    public void run() {
        Author author = new Author();
        author.setNationality("Brazilian");
        author.setName("Jon");
        author.setBirthDate(LocalDate.of(1951, 1, 31));

        authorRepository.saveAndFlush(author);

        Book book = new Book();
        book.setIsbn("4151651-4564");
        book.setPrice(BigDecimal.valueOf(100));
        book.setGenre(Genre.FICTION);
        book.setTitle("Search");
        book.setReleaseDate(LocalDate.of(1980, 1, 2));

        bookRepository.saveAndFlush(book);
    }
}
