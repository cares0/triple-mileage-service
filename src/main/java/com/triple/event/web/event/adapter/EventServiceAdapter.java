package com.triple.event.web.event.adapter;

import com.triple.event.domain.event.EventService;
import com.triple.event.web.event.request.EventRequest;

import java.util.Map;

public interface EventServiceAdapter {

    boolean support(EventService eventService);

    Map<String, String> adapt(EventRequest eventRequest, EventService eventService);

}
