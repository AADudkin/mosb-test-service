package ru.mosb.client.validator.strategy;

import org.springframework.stereotype.Component;
import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.dto.XSource;
import ru.mosb.client.exception.ClientSourceValidationException;

import static org.apache.commons.lang3.StringUtils.*;

@Component
public class MobileClientValidationStrategy implements ClientValidationStrategy {
    @Override
    public XSource forSource() {
        return XSource.MOBILE;
    }

    @Override
    public void validate(ClientDto dto) {
        if (isEmpty(dto.getPhoneNumber())) {
            throw new ClientSourceValidationException("phone number is mandatory", XSource.MOBILE);
        }
    }
}
