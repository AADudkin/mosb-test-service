package ru.mosb.client.validator.strategy;

import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.dto.XSource;

public interface ClientValidationStrategy {

    XSource forSource();

    void validate(ClientDto dto);
}
