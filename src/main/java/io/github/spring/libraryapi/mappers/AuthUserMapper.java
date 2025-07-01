package io.github.spring.libraryapi.mappers;

import io.github.spring.libraryapi.dto.authUserDTO.AuthUserRequestDTO;
import io.github.spring.libraryapi.dto.authUserDTO.AuthUserResponseDTO;
import io.github.spring.libraryapi.model.AuthUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AuthUserMapper {

    public abstract AuthUser toEntity(AuthUserRequestDTO authUserRequestDTO);

    public abstract AuthUserResponseDTO toResponseAuthUserDTO(AuthUser user);
}
