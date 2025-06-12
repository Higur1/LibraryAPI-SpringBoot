package io.github.spring.libraryapi.service;

import io.github.spring.libraryapi.exceptions.OperationNotAllowedException;
import io.github.spring.libraryapi.model.Author;
import io.github.spring.libraryapi.repository.AuthorRepository;
import io.github.spring.libraryapi.repository.BookRepository;
import io.github.spring.libraryapi.validator.AuthorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository repository;
    private final AuthorValidator validator;
    private final BookRepository bookRepository;

    //@RequiredArgsConstructor
    /*public AuthorService(AuthorRepository repository,  AuthorValidator validator, BookRepository bookRepository){
        this.repository = repository;
        this.validator = validator;
        this.bookRepository = bookRepository;
    }*/

    public Author save(Author author) {
        validator.validate(author);
        return repository.save(author);
    }

    public Optional<Author> findByUUID(UUID id) {
        return repository.findById(id);
    }

    public void delete(Author author) {
        if (hasBooks(author)) {
            throw new OperationNotAllowedException("Is not allowed to delete an author who has books");
        }
        repository.delete(author);
    }

    //use searchWithExamples is better
    public List<Author> search(String name, String nationality) {
        if (name != null && nationality != null) {
            return repository.findByNameAndNationality(name, nationality);
        }
        if (name != null) {
            return repository.findByName(name);
        }
        if (nationality != null) {
            return repository.findByNationality(nationality);
        }

        return repository.findAll();
    }

    //same search to filter but using Example
    public List<Author> searchWithExample(String name, String nationality) {

        var author = new Author();
        author.setName(name);
        author.setNationality(nationality);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withIgnorePaths("id", "birthDate")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Author> authorExample = Example.of(author, matcher);

        return repository.findAll(authorExample);
    }

    public void update(Author author) {
        if (author.getId() == null) {
            throw new IllegalArgumentException("Requires an author already registered! ");
        }
        validator.validate(author);
        repository.save(author);
    }

    public boolean hasBooks(Author author) {
        return bookRepository.existsByAuthor(author);
    }
}
