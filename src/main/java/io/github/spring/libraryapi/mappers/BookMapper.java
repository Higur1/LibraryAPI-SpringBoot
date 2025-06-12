package io.github.spring.libraryapi.mappers;

import io.github.spring.libraryapi.dto.bookDTO.RequestBookDTO;
import io.github.spring.libraryapi.dto.bookDTO.ResponseBookDTO;
import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.repository.AuthorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", /*inject others mappers*/ uses = AuthorMapper.class)
public abstract class BookMapper {

    @Autowired
    AuthorRepository authorRepository;

    @Mapping(target = "author", expression = "java( authorRepository.findById(requestBookDTO.authorId()).orElse(null))")
    public abstract  Book toEntity(RequestBookDTO requestBookDTO);

    @Mapping(target = "author", source = "author")
    public abstract ResponseBookDTO toResponseBookDTO(Book book);
}
