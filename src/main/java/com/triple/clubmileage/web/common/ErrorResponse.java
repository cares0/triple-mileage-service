package com.triple.clubmileage.web.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter @Setter
public class ErrorResponse {

    private LocalDateTime time;
    private String code;
    private String message;
    private int status;

    @Builder
    private ErrorResponse(String code, String message, int status) {
        time = LocalDateTime.now();
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
