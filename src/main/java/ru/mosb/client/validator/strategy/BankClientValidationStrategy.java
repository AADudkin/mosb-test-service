package ru.mosb.client.validator.strategy;

import org.springframework.stereotype.Component;
import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.dto.XSource;
import ru.mosb.client.exception.ClientSourceValidationException;

import static org.apache.commons.lang3.StringUtils.*;

@Component
public class BankClientValidationStrategy implements ClientValidationStrategy{
    @Override
    public XSource forSource() {
        return XSource.BANK;
    }

    @Override
    public void validate(ClientDto dto) {
        if (isEmpty(dto.getBankId())) {
            throw new ClientSourceValidationException("bank_id is mandatory", XSource.BANK);
        }
        if (isEmpty(dto.getFirstName())) {
            throw new ClientSourceValidationException("first name is mandatory", XSource.BANK);
        }
        if (isEmpty(dto.getLastName())) {
            throw new ClientSourceValidationException("last name is mandatory", XSource.BANK);
        }
        if (isEmpty(dto.getPatronymic())) {
            throw new ClientSourceValidationException("patronymic", XSource.BANK);
        }
        if (dto.getDateOfBirth() == null) {
            throw new ClientSourceValidationException("date of birth is mandatory", XSource.BANK);
        }
        if (isEmpty(dto.getPassport())) {
            throw new ClientSourceValidationException("passport is mandatory", XSource.BANK);
        }
    }
}
