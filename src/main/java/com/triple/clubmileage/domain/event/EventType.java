package com.triple.clubmileage.domain.event;

public enum EventType {

    REVIEW("eventServiceReviewImpl"),
    FLIGHT("eventServiceFlightImpl"); // 확장 포인트 예제를 위한 열거형입니다.

    private final String implName;

    EventType(String implName) {
        this.implName = implName;
    }

    public String getImplName() {
        return implName;
    }
}
