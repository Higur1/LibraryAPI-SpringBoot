package io.github.spring.libraryapi.controller;

import io.github.spring.libraryapi.dto.authUserDTO.AuthUserRequestDTO;
import io.github.spring.libraryapi.mappers.AuthUserMapper;
import io.github.spring.libraryapi.model.AuthUser;
import io.github.spring.libraryapi.service.AuthUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authUsers")
@RequiredArgsConstructor
public class AuthUserController {

    private final AuthUserService service;
    private final AuthUserMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid AuthUserRequestDTO authUserRequestDTO) {
        AuthUser user = mapper.toEntity(authUserRequestDTO);
        service.save(user);
    }
}
