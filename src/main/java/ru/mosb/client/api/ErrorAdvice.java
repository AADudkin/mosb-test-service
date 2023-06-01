package ru.mosb.client.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.mosb.client.dto.ErrorResponse;
import ru.mosb.client.exception.ClientSourceValidationException;
import ru.mosb.client.exception.NoSuchSourceException;
import ru.mosb.client.exception.UnknownEntityException;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ErrorAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ClientSourceValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleClientSourceValidationException(ClientSourceValidationException e) {
        log.error("ClientSourceValidationException", e);
        return ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(UnknownEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleClientSourceValidationException(UnknownEntityException e) {
        log.error("UnknownEntityException", e);
        return ErrorResponse.builder()
                .code("NOT_FOUND")
                .message(String.format("Клиент с ИД = %d не найден", e.getId()))
                .build();
    }

    @ExceptionHandler(NoSuchSourceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoSuchSourceException(NoSuchSourceException e) {
        log.error("NoSuchSourceException", e);
        return ErrorResponse.builder()
                .code("BAD_REQUEST")
                .message(String.format("Источник %s не зарегистрирован", e.getSource()))
                .build();
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status,
                                                                  @NotNull WebRequest request) {
        log.error("MethodArgumentNotValidException", e);

        Map<String, String> fieldErrors = e.getFieldErrors().stream().collect(Collectors.toMap(
                FieldError::getField,
                DefaultMessageSourceResolvable::getDefaultMessage
        ));

        ErrorResponse responseBody = ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .fieldErrors(fieldErrors)
                .build();

        return handleExceptionInternal(e, responseBody, headers, status, request);
    }
}
