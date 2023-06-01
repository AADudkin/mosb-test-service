package ru.mosb.client.exception;

import lombok.Getter;
import ru.mosb.client.dto.XSource;

public class ClientSourceValidationException extends RuntimeException {

    @Getter
    private XSource xSource;

    public ClientSourceValidationException(String message, XSource source) {
        super(message);
        this.xSource = source;
    }
}
