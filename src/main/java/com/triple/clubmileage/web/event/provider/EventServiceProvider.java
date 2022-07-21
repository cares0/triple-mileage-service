package com.triple.clubmileage.web.event.provider;

import com.triple.clubmileage.domain.event.service.EventService;
import com.triple.clubmileage.domain.event.EventType;
import com.triple.clubmileage.web.event.request.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventServiceProvider {

    private final Map<EventType, EventService> eventServiceMap;

    public EventService getEventService(EventRequest eventRequest) {
        return eventServiceMap.get(eventRequest.getType().getImplName());
    }
}
