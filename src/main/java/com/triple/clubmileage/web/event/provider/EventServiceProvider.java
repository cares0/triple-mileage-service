package com.triple.clubmileage.web.event.provider;

import com.triple.clubmileage.domain.event.service.EventService;
import com.triple.clubmileage.domain.event.service.EventServiceFlightImpl;
import com.triple.clubmileage.domain.event.service.EventServiceReviewImpl;
import com.triple.clubmileage.domain.event.EventType;
import com.triple.clubmileage.web.event.request.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventServiceProvider {

    private final Map<EventType, EventService> eventServiceMap;

    @Autowired
    public EventServiceProvider(EventServiceReviewImpl eventServiceReviewImpl,
                                EventServiceFlightImpl eventServiceFlightImpl) {
        eventServiceMap = new HashMap<>();
        eventServiceMap.put(EventType.REVIEW, eventServiceReviewImpl);
        eventServiceMap.put(EventType.FLIGHT, eventServiceFlightImpl); // 확장 포인트 예제
    }

    public EventService getEventService(EventRequest eventRequest) {
        return eventServiceMap.get(eventRequest.getType());
    }
}
