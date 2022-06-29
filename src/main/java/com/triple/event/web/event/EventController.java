package com.triple.event.web.event;

import com.triple.event.domain.event.*;
import com.triple.event.web.event.adapter.EventServiceAdapter;
import com.triple.event.web.event.provider.EventServiceAdapterProvider;
import com.triple.event.web.event.provider.EventServiceProvider;
import com.triple.event.web.event.request.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventServiceProvider eventServiceProvider;
    private final EventServiceAdapterProvider eventServiceAdapterProvider;

    @PostMapping
    public String eventAdd(@RequestBody EventRequest eventRequest) {
        EventService eventService = eventServiceProvider.getEventService(eventRequest);
        EventServiceAdapter eventServiceAdapter = eventServiceAdapterProvider.getEventServiceAdapter(eventService);
        eventServiceAdapter.adapt(eventRequest, eventService);

        return "ok";
    }

}
