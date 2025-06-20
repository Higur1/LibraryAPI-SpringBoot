package io.github.spring.libraryapi.mappers;

import io.github.spring.libraryapi.dto.bookDTO.BookRequestDTO;
import io.github.spring.libraryapi.dto.bookDTO.BookResponseDTO;
import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.repository.AuthorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", /*inject others mappers*/ uses = AuthorMapper.class)
public abstract class BookMapper {

    @Autowired
    AuthorRepository authorRepository;

    @Mapping(target = "author", expression = "java(authorRepository.findById(bookRequestDTO.authorId()).orElse(null))")
    public abstract Book toEntity(BookRequestDTO bookRequestDTO);

    @Mapping(target = "authUsername", expression = "java(book.getAuthUser().getLogin())")
    public abstract BookResponseDTO toResponseBookDTO(Book book);
}
