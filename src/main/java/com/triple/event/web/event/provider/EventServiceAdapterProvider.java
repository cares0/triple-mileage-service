package com.triple.event.web.event.provider;

import com.triple.event.domain.event.EventService;
import com.triple.event.web.event.adapter.EventServiceAdapter;
import com.triple.event.web.event.adapter.EventServiceReviewAdapter;
import com.triple.event.web.exception.AdapterNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventServiceAdapterProvider {

    private final List<EventServiceAdapter> eventServiceAdapters;

    @Autowired
    public EventServiceAdapterProvider(EventServiceReviewAdapter eventServiceReviewAdapter) {
        List<EventServiceAdapter> eventServiceAdapterList = new ArrayList<>();
        eventServiceAdapterList.add(eventServiceReviewAdapter);
        this.eventServiceAdapters = eventServiceAdapterList;
    }

    public EventServiceAdapter getEventServiceAdapter(EventService eventService) {
        for (EventServiceAdapter eventServiceAdapter : eventServiceAdapters) {
            if (eventServiceAdapter.support(eventService)) {
                return eventServiceAdapter;
            }
        }
        throw new AdapterNotFoundException("맞는 어댑터를 찾을 수 없음");
    }
}
