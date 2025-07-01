package io.github.spring.libraryapi.mappers;

import io.github.spring.libraryapi.dto.clientDTO.ClientRequestDTO;
import io.github.spring.libraryapi.dto.clientDTO.ClientResponseDTO;
import io.github.spring.libraryapi.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {
    public abstract Client toEntity(ClientRequestDTO clientRequestDTO);

    public abstract ClientResponseDTO toResponseClientDTO(Client client);
}
