package com.triple.event.web.event.provider;

import com.triple.event.domain.event.EventService;
import com.triple.event.domain.event.EventServiceReviewImpl;
import com.triple.event.domain.event.EventType;
import com.triple.event.web.event.request.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventServiceProvider {

    private final Map<EventType, EventService> eventServiceMap;

    @Autowired
    public EventServiceProvider(EventServiceReviewImpl eventServiceReviewImpl) {
        eventServiceMap = new HashMap<>();
        eventServiceMap.put(EventType.REVIEW, eventServiceReviewImpl);
    }

    public EventService getEventService(EventRequest eventRequest) {
        return eventServiceMap.get(eventRequest.getType());
    }
}
