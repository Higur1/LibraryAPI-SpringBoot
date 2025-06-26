package io.github.spring.libraryapi.validator;

import io.github.spring.libraryapi.exceptions.DuplicateRecordException;
import io.github.spring.libraryapi.model.Author;
import io.github.spring.libraryapi.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorValidator {
    private final AuthorRepository repository;

    public void validate(Author author) {
        if (hasAuthor(author)) {
            throw new DuplicateRecordException("Author already exists");
        }
    }

    private boolean hasAuthor(Author author) {
        return repository.findByNameAndBirthDateAndNationality(
                author.getName(),
                author.getBirthDate(),
                author.getNationality()
        ).map(found -> !author.getId().equals(found.getId())
        ).orElse(false);
    }
}
