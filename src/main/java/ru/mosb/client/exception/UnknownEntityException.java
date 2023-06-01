package ru.mosb.client.exception;

import lombok.Getter;

public class UnknownEntityException extends RuntimeException {

    @Getter
    private Long id;

    public UnknownEntityException(String message, Long id) {
        super(message);
        this.id = id;
    }
}
