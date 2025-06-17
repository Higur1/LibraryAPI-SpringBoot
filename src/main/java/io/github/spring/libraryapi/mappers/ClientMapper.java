package io.github.spring.libraryapi.mappers;

import io.github.spring.libraryapi.dto.clientDTO.RequestClientDTO;
import io.github.spring.libraryapi.dto.clientDTO.ResponseClientDTO;
import io.github.spring.libraryapi.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {
    public abstract Client toEntity(RequestClientDTO requestClientDTO);
    public abstract ResponseClientDTO toResponseClientDTO(Client client);
}
