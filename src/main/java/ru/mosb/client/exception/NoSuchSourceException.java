package ru.mosb.client.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NoSuchSourceException extends RuntimeException {

    @Getter
    private final String source;
}
