package io.github.spring.libraryapi.controller;

import io.github.spring.libraryapi.dto.authUserDTO.AuthUserRequestDTO;
import io.github.spring.libraryapi.mappers.AuthUserMapper;
import io.github.spring.libraryapi.model.AuthUser;
import io.github.spring.libraryapi.service.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authUsers")
@RequiredArgsConstructor
@Tag(name = "AuthUser")
public class AuthUserController {

    private final AuthUserService service;
    private final AuthUserMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save", description = "Register new AuthUser")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Register successfully."),
            @ApiResponse(responseCode = "409", description = "AuthUser already registered."),
            @ApiResponse(responseCode = "422", description = "Validation error.")
        }
    )
    public void save(@RequestBody @Valid AuthUserRequestDTO authUserRequestDTO) {
        AuthUser user = mapper.toEntity(authUserRequestDTO);
        service.save(user);
    }
}
