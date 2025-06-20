package io.github.spring.libraryapi.controller;

import io.github.spring.libraryapi.dto.clientDTO.ClientRequestDTO;
import io.github.spring.libraryapi.mappers.ClientMapper;
import io.github.spring.libraryapi.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
@PreAuthorize("hasRole('MANAGER')")
public class ClientController {

    private final ClientService service;
    private final ClientMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody ClientRequestDTO clientRequestDTO) {
        service.save(mapper.toEntity(clientRequestDTO));
    }
}
