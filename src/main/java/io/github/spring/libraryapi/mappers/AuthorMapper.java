package io.github.spring.libraryapi.mappers;

import io.github.spring.libraryapi.dto.authorDTO.AuthorRequestDTO;
import io.github.spring.libraryapi.dto.authorDTO.AuthorResponseDTO;
import io.github.spring.libraryapi.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuthUserMapper.class)
public abstract class AuthorMapper {

    public abstract Author toEntity(AuthorRequestDTO authorRequestDTO);

    @Mapping(target = "authUsername", expression = "java(author.getAuthUser().getLogin())")
    public abstract AuthorResponseDTO toResponseAuthorDTO(Author author);
}
