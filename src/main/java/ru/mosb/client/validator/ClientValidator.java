package ru.mosb.client.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.dto.XSource;
import ru.mosb.client.validator.strategy.ClientValidationStrategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ClientValidator{

    private final Map<XSource, ClientValidationStrategy> validationStrategies;

    public ClientValidator(@Autowired List<ClientValidationStrategy> validationStrategies) {
        this.validationStrategies = validationStrategies.stream()
                .collect(Collectors.toMap(
                        ClientValidationStrategy::forSource,
                        Function.identity()
                ));
    }

    public void validate(ClientDto clientDto, XSource forSource) {
        validationStrategies.get(forSource).validate(clientDto);
    }
}
