package io.github.spring.libraryapi.controller;

import io.github.spring.libraryapi.dto.authUser.RequestAuthUserDTO;
import io.github.spring.libraryapi.mappers.AuthUserMapper;
import io.github.spring.libraryapi.model.AuthUser;
import io.github.spring.libraryapi.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authUsers")
@RequiredArgsConstructor
public class AuthUserController {

    private final AuthUserService service;
    private final AuthUserMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER')")
    public void save(@RequestBody RequestAuthUserDTO requestAuthUserDTO) {
        AuthUser user = mapper.toEntity(requestAuthUserDTO);
        service.save(user);
    }
}
