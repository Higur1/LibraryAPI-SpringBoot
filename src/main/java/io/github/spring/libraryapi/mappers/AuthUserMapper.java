package io.github.spring.libraryapi.mappers;

import io.github.spring.libraryapi.dto.authUser.RequestAuthUserDTO;
import io.github.spring.libraryapi.dto.authUser.ResponseAuthUserDTO;
import io.github.spring.libraryapi.model.AuthUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AuthUserMapper {

    public abstract AuthUser toEntity(RequestAuthUserDTO requestAuthUserDTO);
    public abstract ResponseAuthUserDTO toResponseAuthUserDTO(AuthUser user);
}
