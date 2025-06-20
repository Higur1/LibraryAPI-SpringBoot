package io.github.spring.libraryapi.controller;

import io.github.spring.libraryapi.dto.bookDTO.BookRequestDTO;
import io.github.spring.libraryapi.dto.bookDTO.BookResponseDTO;
import io.github.spring.libraryapi.mappers.BookMapper;
import io.github.spring.libraryapi.model.Book;
import io.github.spring.libraryapi.model.Genre;
import io.github.spring.libraryapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Save", description = "Register new book.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Register successfully."),
            @ApiResponse(responseCode = "422", description = "Validation error."),
            @ApiResponse(responseCode = "409", description = "Book already registered.")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid BookRequestDTO bookRequestDTO) {

        Book book = mapper.toEntity(bookRequestDTO);
        service.save(book);

        return ResponseEntity.created(generateHeaderLocation(book.getId())).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Find" , description = "Find the book by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns the found book."),
            @ApiResponse(responseCode = "404", description = "Book not found.")
    })
    public ResponseEntity<BookResponseDTO> find(@PathVariable("id") @Valid UUID id) {
        return service.findByUUID(id).map(book -> {
            BookResponseDTO responseBookDTO = mapper.toResponseBookDTO(book);
            return ResponseEntity.ok(responseBookDTO);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Search" , description = "Search books by parameters.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns the found books."),
    })
    public ResponseEntity<Page<BookResponseDTO>> search(
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
        Page<BookResponseDTO> mapPageResponseBookDTO = pageResult.map(mapper::toResponseBookDTO);

        return ResponseEntity.ok(mapPageResponseBookDTO);
    }


    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Update" , description = "Update existing book.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Update successfully."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
            @ApiResponse(responseCode = "409", description = "Book already exists."),

    })
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody @Valid BookRequestDTO bookRequestDTO) {

        return service.findByUUID(id).map(book -> {
            Book entityAux = mapper.toEntity(bookRequestDTO);
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
    @Operation(summary = "Delete" , description = "Deletes an existing book.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Delete successfully."),
            @ApiResponse(responseCode = "404", description = "Book not found.")
    })
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        return service.findByUUID(id).map(book -> {
            service.delete(book);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

