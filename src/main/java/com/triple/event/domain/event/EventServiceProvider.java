package com.triple.event.domain.event;

import com.triple.event.web.event.request.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventServiceProvider {

    private final List<EventService> eventServiceList;

    @Autowired
    public EventServiceProvider(EventServiceReviewImpl eventServiceReviewImpl) {
        eventServiceList = new ArrayList<>();
        eventServiceList.add(eventServiceReviewImpl);
    }

    public EventService getEventService(EventRequest eventRequest) {
        for (EventService eventService : eventServiceList) {
            if (eventService.support(eventRequest)) {
                return eventService;
            }
        }
        throw new IllegalStateException("유효한 타입이 아님");
    }
}
