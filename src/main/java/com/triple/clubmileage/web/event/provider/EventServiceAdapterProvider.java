package com.triple.clubmileage.web.event.provider;

import com.triple.clubmileage.domain.event.service.EventService;
import com.triple.clubmileage.web.event.adapter.EventServiceAdapter;
import com.triple.clubmileage.web.event.adapter.EventServiceFlightAdapter;
import com.triple.clubmileage.web.event.adapter.EventServiceReviewAdapter;
import com.triple.clubmileage.web.exception.AdapterNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventServiceAdapterProvider {

    private final List<EventServiceAdapter> eventServiceAdapters;

    @Autowired
    public EventServiceAdapterProvider(EventServiceReviewAdapter eventServiceReviewAdapter,
                                       EventServiceFlightAdapter eventServiceFlightAdapter) {
        List<EventServiceAdapter> eventServiceAdapterList = new ArrayList<>();
        eventServiceAdapterList.add(eventServiceReviewAdapter);
        eventServiceAdapterList.add(eventServiceFlightAdapter);
        this.eventServiceAdapters = eventServiceAdapterList;
    }

    public EventServiceAdapter getEventServiceAdapter(EventService eventService) {
        for (EventServiceAdapter eventServiceAdapter : eventServiceAdapters) {
            if (eventServiceAdapter.support(eventService)) {
                return eventServiceAdapter;
            }
        }
        throw new AdapterNotFoundException("해당 EventService와 맞는 어댑터를 찾을 수 없음");
    }
}
