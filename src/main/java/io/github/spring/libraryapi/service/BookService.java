package io.github.spring.libraryapi.service;

import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.model.Genre;
import io.github.spring.libraryapi.repository.BookRepository;
import io.github.spring.libraryapi.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static io.github.spring.libraryapi.specs.BookSpecification.*;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repository;
    private final BookValidator validator;

    public void save(Book book) {
        validator.validate(book);
        repository.save(book);
    }

    public Optional<Book> findByUUID(UUID id) {
        return repository.findById(id);
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    public Page<Book> search(
            String isbn,
            String title,
            String authorName,
            Genre genre,
            Integer releaseDate,
            Integer page,
            Integer pageSize
    ) {
        /*Specification<Book> specification = Specification
                .where(BookSpecification.isbnEqual(isbn))
                .and(BookSpecification.titleLike(title))
                .and(BookSpecification.genreEqual(genre));*/

        Specification<Book> specification = Specification
                .where((root, query, criteriaBuilder)
                        -> criteriaBuilder.conjunction());
        if (isbn != null) {
            specification = specification.and(isbnEqual(isbn));
        }
        if (title != null) {
            specification = specification.and(titleLike(title));
        }
        if (genre != null) {
            specification = specification.and(genreEqual(genre));
        }
        if (releaseDate != null) {
            specification = specification.and(releaseDateEqual(releaseDate));
        }
        if (authorName != null) {
            specification = specification.and(authorNameLike(authorName));
        }

        Pageable pageable = PageRequest.of(page, pageSize);

        return repository.findAll(specification, pageable);
    }

    public void update(Book book) {
        if (book.getId() == null) {
            throw new IllegalArgumentException("To update, the book must already be registered.");
        }
        validator.validate(book);
        repository.save(book);
    }
}
