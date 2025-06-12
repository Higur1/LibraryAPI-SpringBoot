package io.github.spring.libraryapi.validator;

import io.github.spring.libraryapi.exceptions.DuplicateRecordException;
import io.github.spring.libraryapi.exceptions.InvalidFieldException;
import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookValidator {

    private static final int PRICE_REQUIRED_FROM_YEAR = 2020;
    private final BookRepository repository;

    public void validate(Book book){
        if(isDuplicateIsbn(book)){
            throw new DuplicateRecordException("Duplicate record: ISBN '" + book.getIsbn() + "' is already registered.");
        }
        if(isRequiredPriceNotProvided(book)){
            throw new InvalidFieldException("price", "For books published in 2020 or later, the price is required.");
        }
    }
    private boolean isDuplicateIsbn(Book book){
        Optional<Book> foundBook = repository.findByIsbn(book.getIsbn());
        if(book.getId() == null){
            return foundBook.isPresent();
        }
        return foundBook
                .map(Book::getId)
                .stream()
                .anyMatch(id -> !id.equals(book.getId()));
    }
    //if release date after 2020 field price is required
    private boolean isRequiredPriceNotProvided(Book book){
        return book.getPrice() == null && book.getReleaseDate().getYear() >= PRICE_REQUIRED_FROM_YEAR;
    }
}
