package com.triple.clubmileage.web.event;

import com.triple.clubmileage.domain.event.exception.InvalidModifyingFactorException;
import com.triple.clubmileage.web.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice(assignableTypes = {EventController.class})
public class EventExControllerAdvice {

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse InvalidModifyingFactorExHandler(InvalidModifyingFactorException e) {
        log.error("[InvalidModifyingFactorException]", e);
        return ErrorResponse.builder()
                .code("InvalidModifyingFactor")
                .message(e.getMessage())
                .status(INTERNAL_SERVER_ERROR.value()).build();
    }

}
