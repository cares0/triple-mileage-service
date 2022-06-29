package com.triple.event.domain.event;

import com.triple.event.web.event.request.EventRequest;

public interface EventService {

    boolean support(EventRequest eventRequest);

}
