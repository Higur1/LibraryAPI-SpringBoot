package io.github.spring.libraryapi.controller;

import io.github.spring.libraryapi.dto.clientDTO.ClientRequestDTO;
import io.github.spring.libraryapi.mappers.ClientMapper;
import io.github.spring.libraryapi.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
@PreAuthorize("hasRole('MANAGER')")
@Tag(name = "Clients")
public class ClientController {

    private final ClientService service;
    private final ClientMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save", description = "Register new client.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Register successfully."),
            @ApiResponse(responseCode = "409", description = "Client already exists."),
            @ApiResponse(responseCode = "422", description = "Validation error.")
    })
    public void save(@RequestBody ClientRequestDTO clientRequestDTO) {
        service.save(mapper.toEntity(clientRequestDTO));
    }
}
