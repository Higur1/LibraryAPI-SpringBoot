package io.github.spring.libraryapi.controller;

import io.github.spring.libraryapi.dto.authorDTO.RequestAuthorDTO;
import io.github.spring.libraryapi.dto.authorDTO.ResponseAuthorDTO;
import io.github.spring.libraryapi.mappers.AuthorMapper;
import io.github.spring.libraryapi.model.Author;
import io.github.spring.libraryapi.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController extends GenericController {

    private final AuthorService service;
    private final AuthorMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> save(@RequestBody @Valid RequestAuthorDTO dto) {

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
    public ResponseEntity<ResponseAuthorDTO> find(@PathVariable("id") UUID id) {

        return service.findByUUID(id).map(author -> {
            ResponseAuthorDTO responseAuthorDTO = mapper.toResponseAuthorDTO(author);
            return ResponseEntity.ok(responseAuthorDTO);
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
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        return service.findByUUID(id).map(author -> {
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
    public ResponseEntity<List<ResponseAuthorDTO>> search(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "nationality", required = false) String nationality) {
        return ResponseEntity
                .ok(service.search(name, nationality)
                        .stream()
                        .map(mapper::toResponseAuthorDTO)
                        .collect(Collectors.toList()));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid RequestAuthorDTO requestAuthorDTO) {

        return service.findByUUID(id).map(author -> {
            author.setName(requestAuthorDTO.name());
            author.setNationality(requestAuthorDTO.nationality());
            author.setBirthDate(requestAuthorDTO.birthDate());
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
