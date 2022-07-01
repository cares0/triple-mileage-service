package com.triple.clubmileage.web.event.adapter;

import com.triple.clubmileage.domain.event.service.EventService;
import com.triple.clubmileage.web.event.request.EventRequest;

import java.util.Map;

public interface EventServiceAdapter {

    boolean support(EventService eventService);

    Map<String, String> add(EventRequest eventRequest, EventService eventService);

}
