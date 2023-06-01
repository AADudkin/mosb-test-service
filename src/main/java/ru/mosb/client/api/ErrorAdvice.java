package ru.mosb.client.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.mosb.client.dto.ErrorResponse;
import ru.mosb.client.exception.ClientSourceValidationException;
import ru.mosb.client.exception.UnknownEntityException;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ErrorAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ClientSourceValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleClientSourceValidationException(ClientSourceValidationException e) {
        return ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(UnknownEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleClientSourceValidationException(UnknownEntityException e) {
        return ErrorResponse.builder()
                .code("NOT_FOUND")
                .message(String.format("Клиент с ИД = %d не найден", e.getId()))
                .build();
    }
}
