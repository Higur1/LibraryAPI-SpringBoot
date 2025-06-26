package io.github.spring.libraryapi.controller;

import io.github.spring.libraryapi.dto.authorDTO.AuthorRequestDTO;
import io.github.spring.libraryapi.dto.authorDTO.AuthorResponseDTO;
import io.github.spring.libraryapi.mappers.AuthorMapper;
import io.github.spring.libraryapi.model.Author;
import io.github.spring.libraryapi.security.CurrentUserService;
import io.github.spring.libraryapi.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
@Tag(name = "Authors")
@Slf4j
public class AuthorController extends GenericController {

    private final AuthorService service;
    private final AuthorMapper mapper;
    private final CurrentUserService userService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Save", description = "Register new author.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Register successfully."),
            @ApiResponse(responseCode = "409", description = "Author already registered."),
            @ApiResponse(responseCode = "422", description = "Validation error.")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid AuthorRequestDTO dto) {
        log.info("Registering new author: {} by: {}", dto.name(), userService.getCurrentUserName());

        /*UserDetails userLogged = (UserDetails) authentication.getPrincipal();
        AuthUser authUser = authUserService.getByLogin(userLogged.getUsername());
        Author author = mapper.toEntity(dto);
        author.setUser_id(authUser.getId());*/

        Author author = mapper.toEntity(dto);

        service.save(author);

        return ResponseEntity.created(generateHeaderLocation(author.getId())).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Find", description = "Find the author by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns the found author."),
            @ApiResponse(responseCode = "404", description = "Author not found.")
    })
    public ResponseEntity<AuthorResponseDTO> find(@PathVariable("id") UUID id) {

        return service.findByUUID(id).map(author -> {
            AuthorResponseDTO authorResponseDTO = mapper.toResponseAuthorDTO(author);
            return ResponseEntity.ok(authorResponseDTO);
        }).orElseGet(() -> ResponseEntity.notFound().build());

        //old way
       /* var uuidAuthor = UUID.fromString(id);

        Optional<Author> resultAuthor = service.authorDetailById(uuidAuthor);

        if (resultAuthor.isPresent()) {
            Author author = resultAuthor.get();
            ResponseAuthorDTO responseAuthorDTO = mapper.toResponseAuthorDTO(author);
            return ResponseEntity.ok(responseAuthorDTO);
        }

        return ResponseEntity.notFound().build();*/
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Delete", description = "Deletes an existing author.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Delete successfully."),
            @ApiResponse(responseCode = "404", description = "Author not found."),
            @ApiResponse(responseCode = "404", description = "It is not possible to delete an author with a registered book.")
    })
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {

        return service.findByUUID(id).map(author -> {
            log.info("Deleting author id: {} name: {} by: {}", author.getId(), author.getName(), userService.getCurrentUserName());
            service.delete(author);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());

            /*UUID uuid = UUID.fromString(id);
            Optional<Author> resultAuthor = service.authorDetailById(uuidAuthor);

            if (resultAuthor.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            service.delete(resultAuthor.get());

            return ResponseEntity.noContent().build();*/
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    @Operation(summary = "Search", description = "Search authors by parameters.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search successfully.")
    })
    public ResponseEntity<List<AuthorResponseDTO>> search(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "nationality", required = false) String nationality) {
        return ResponseEntity
                .ok(service.search(name, nationality)
                        .stream()
                        .map(mapper::toResponseAuthorDTO)
                        .collect(Collectors.toList()));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update", description = "Update existing author.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Update successfully."),
            @ApiResponse(responseCode = "404", description = "Author not found."),
            @ApiResponse(responseCode = "409", description = "Author already exists.")
    })
    public ResponseEntity<?> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid AuthorRequestDTO authorRequestDTO) {

        return service.findByUUID(id).map(author -> {
            author.setName(authorRequestDTO.name());
            author.setNationality(authorRequestDTO.nationality());
            author.setBirthDate(authorRequestDTO.birthDate());
            log.info("Updating author id: {} by: {}", author.getId(), userService.getCurrentUserName());
            service.update(author);

            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());

            /*var uuidAuthor = UUID.fromString(id);

            Optional<Author> resultAuthor = service.authorDetailById(uuidAuthor);

            if (resultAuthor.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var author = resultAuthor.get();
            author.setName(requestAuthorDTO.name());
            author.setNationality(requestAuthorDTO.nationality());
            author.setBirthDate(requestAuthorDTO.birthDate());
            service.update(author);

            return ResponseEntity.noContent().build();*/
    }
}
