package com.triple.clubmileage.domain.event.exception;

public class InvalidModifyingFactorException extends IllegalArgumentException {

    public InvalidModifyingFactorException() {
        super("해당 구현체에서 처리 불가능한 변경 요인입니다. \n 변경 요인 처리 로직을 확인해주세요.");
    }
}
