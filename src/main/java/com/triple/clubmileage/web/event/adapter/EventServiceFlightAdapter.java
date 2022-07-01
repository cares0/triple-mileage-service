package com.triple.clubmileage.web.event.adapter;

import com.triple.clubmileage.domain.event.service.EventService;
import com.triple.clubmileage.domain.event.service.EventServiceFlightImpl;
import com.triple.clubmileage.web.event.request.EventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 확장 포인트 예제를 위한 어댑터입니다.
 */
@Component
@Slf4j
public class EventServiceFlightAdapter implements EventServiceAdapter {

    @Override
    public boolean support(EventService eventService) {
        return eventService instanceof EventServiceFlightImpl;
    }

    @Override
    public Map<String, String> adapt(EventRequest eventRequest, EventService eventService) {
        log.info("[구현체: {}, EventType이 FLIGHT일 경우 여기서 처리]", eventService.getClass());
        EventServiceFlightImpl eventServiceFlightImpl = (EventServiceFlightImpl) eventService;
        // 원하는 파라미터로 로직 처리 후 정해진 리턴 타입으로 맞춰서 반환
        return null;
    }
}
