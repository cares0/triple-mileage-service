package com.triple.clubmileage.web.common;

import com.triple.clubmileage.domain.exception.EntityNotFoundException;
import com.triple.clubmileage.domain.exception.InvalidRequestDataException;
import com.triple.clubmileage.web.exception.AdapterNotFoundException;
import com.triple.clubmileage.web.exception.ValidationFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class CommonExControllerAdvice {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse entityNotFoundExHandler(EntityNotFoundException e) {
        log.error("[EntityNotFoundException]", e);
        return ErrorResponse.builder()
                .code("EntityNotFound")
                .message(e.getMessage())
                .status(NOT_FOUND.value()).build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse ValidationFailExHandler(ValidationFailException e) {
        log.error("[ValidationFailException]", e);
        return ErrorResponse.builder()
                .code("ValidationFail")
                .message(e.getMessage())
                .status(BAD_REQUEST.value()).build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse InvalidRequestDataExHandler(InvalidRequestDataException e) {
        log.error("[InvalidRequestDataException]", e);
        return ErrorResponse.builder()
                .code("InvalidRequestData")
                .message(e.getMessage())
                .status(BAD_REQUEST.value()).build();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse AdapterNotFoundExHandler(AdapterNotFoundException e) {
        log.error("[AdapterNotFoundException]", e);
        return ErrorResponse.builder()
                .code("AdapterNotFound")
                .message(e.getMessage())
                .status(INTERNAL_SERVER_ERROR.value()).build();
    }

}
