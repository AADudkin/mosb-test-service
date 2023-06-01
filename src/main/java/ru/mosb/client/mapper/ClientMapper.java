package ru.mosb.client.mapper;

import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.model.Client;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface ClientMapper {

    ClientDto toDto(Client entity);

    Client toEntity(ClientDto dto);

    List<ClientDto> toDtoList(List<Client> clients);
}
