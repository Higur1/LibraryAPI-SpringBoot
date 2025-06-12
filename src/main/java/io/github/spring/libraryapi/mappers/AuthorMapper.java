package io.github.spring.libraryapi.mappers;

import io.github.spring.libraryapi.dto.authorDTO.RequestAuthorDTO;
import io.github.spring.libraryapi.dto.authorDTO.ResponseAuthorDTO;
import io.github.spring.libraryapi.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AuthorMapper {
    public abstract Author toEntity(RequestAuthorDTO requestAuthorDTO);

    public abstract ResponseAuthorDTO toResponseAuthorDTO(Author author);
}
