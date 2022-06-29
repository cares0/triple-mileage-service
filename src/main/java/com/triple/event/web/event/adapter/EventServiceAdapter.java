package com.triple.event.web.event.adapter;

import com.triple.event.domain.event.EventService;
import com.triple.event.web.event.request.EventRequest;

public interface EventServiceAdapter {

    boolean support(EventService eventService);

    void adapt(EventRequest eventRequest, EventService eventService);

}
