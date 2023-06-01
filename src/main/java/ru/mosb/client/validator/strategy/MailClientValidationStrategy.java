package ru.mosb.client.validator.strategy;

import org.springframework.stereotype.Component;
import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.dto.XSource;
import ru.mosb.client.exception.ClientSourceValidationException;

import static org.apache.commons.lang3.StringUtils.*;

@Component
public class MailClientValidationStrategy implements ClientValidationStrategy{
    @Override
    public XSource forSource() {
        return XSource.MAIL;
    }

    @Override
    public void validate(ClientDto dto) {
        if (isEmpty(dto.getFirstName())) {
            throw new ClientSourceValidationException("first name is mandatory", XSource.MAIL);
        }
        if (isEmpty(dto.getEmail())) {
            throw new ClientSourceValidationException("email is mandatory", XSource.MAIL);
        }
    }
}
