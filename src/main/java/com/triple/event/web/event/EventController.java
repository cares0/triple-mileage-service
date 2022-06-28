package com.triple.event.web.event;

import com.triple.event.domain.event.*;
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

    @PostMapping
    public String eventAdd(@RequestBody EventRequest eventRequest) {
        EventService eventService = eventServiceProvider.getEventService(eventRequest);
        eventService.add(eventRequest);

        return "ok";
    }
}
