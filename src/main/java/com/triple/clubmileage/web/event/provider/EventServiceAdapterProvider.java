package com.triple.clubmileage.web.event.provider;

import com.triple.clubmileage.domain.event.service.EventService;
import com.triple.clubmileage.web.event.adapter.EventServiceAdapter;
import com.triple.clubmileage.web.exception.AdapterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventServiceAdapterProvider {

    private final List<EventServiceAdapter> eventServiceAdapters;

    public EventServiceAdapter getEventServiceAdapter(EventService eventService) {
        return eventServiceAdapters.stream()
                .filter(esa -> esa.support(eventService))
                .findAny()
                .orElseThrow(() -> new AdapterNotFoundException("해당 EventService와 맞는 어댑터를 찾을 수 없음"));
    }
}
