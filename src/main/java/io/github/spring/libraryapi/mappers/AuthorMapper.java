package io.github.spring.libraryapi.mappers;

import io.github.spring.libraryapi.dto.authorDTO.RequestAuthorDTO;
import io.github.spring.libraryapi.dto.authorDTO.ResponseAuthorDTO;
import io.github.spring.libraryapi.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuthUserMapper.class)
public abstract class AuthorMapper {

    public abstract Author toEntity(RequestAuthorDTO requestAuthorDTO);

    @Mapping(target = "authUsername", expression = "java(author.getAuthUser().getLogin())")
    public abstract ResponseAuthorDTO toResponseAuthorDTO(Author author);
}
