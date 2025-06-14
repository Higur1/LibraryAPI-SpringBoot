package io.github.spring.libraryapi.controller;

import io.github.spring.libraryapi.dto.bookDTO.RequestBookDTO;
import io.github.spring.libraryapi.dto.bookDTO.ResponseBookDTO;
import io.github.spring.libraryapi.mappers.BookMapper;
import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.model.Genre;
import io.github.spring.libraryapi.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController extends GenericController {
    private final BookService service;
    private final BookMapper mapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<Void> save(@RequestBody @Valid RequestBookDTO requestBookDTO) {

        Book book = mapper.toEntity(requestBookDTO);
        service.save(book);

        return ResponseEntity.created(generateHeaderLocation(book.getId())).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<ResponseBookDTO> find(@PathVariable("id") @Valid UUID id) {
        return service.findByUUID(id).map(book -> {
            ResponseBookDTO responseBookDTO = mapper.toResponseBookDTO(book);
            return ResponseEntity.ok(responseBookDTO);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<Page<ResponseBookDTO>> search(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "title", required = false)
            String title,
            @RequestParam(value = "genre", required = false)
            Genre genre,
            @RequestParam(value = "authorName", required = false)
            String nameAuthor,
            @RequestParam(value = "releaseDate", required = false)
            Integer releaseDate,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10")
            Integer pageSize
    ) {
        Page<Book> pageResult = service.search(isbn, title, nameAuthor, genre, releaseDate, page, pageSize);
        Page<ResponseBookDTO> mapPageResponseBookDTO = pageResult.map(mapper::toResponseBookDTO);

        return ResponseEntity.ok(mapPageResponseBookDTO);
    }


    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody @Valid RequestBookDTO requestBookDTO) {

        return service.findByUUID(id).map(book -> {
            Book entityAux = mapper.toEntity(requestBookDTO);
            book.setReleaseDate(entityAux.getReleaseDate());
            book.setIsbn(entityAux.getIsbn());
            book.setPrice(entityAux.getPrice());
            book.setGenre(entityAux.getGenre());
            book.setTitle(entityAux.getTitle());
            book.setAuthor(entityAux.getAuthor());

            service.update(book);

            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        return service.findByUUID(id).map(book -> {
            service.delete(book);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

