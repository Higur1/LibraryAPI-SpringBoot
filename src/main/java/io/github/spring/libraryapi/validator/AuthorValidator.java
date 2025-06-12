package io.github.spring.libraryapi.validator;

import io.github.spring.libraryapi.exceptions.DuplicateRecordException;
import io.github.spring.libraryapi.model.Author;
import io.github.spring.libraryapi.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorValidator {
    private final AuthorRepository repository;

    public void validate(Author author){
        if(hasAuthor(author)){
            throw  new DuplicateRecordException("Author already exists");
        }
    }

    private boolean hasAuthor(Author author){
        Optional<Author> authorResultFind = repository.findByNameAndBirthDateAndNationality(
                author.getName(),
                author.getBirthDate(),
                author.getNationality()
        );

        if(author.getId() == null){
            return authorResultFind.isPresent();
        }

        return !author.getId().equals(authorResultFind.get().getId()) && authorResultFind.isPresent();
    }
}
