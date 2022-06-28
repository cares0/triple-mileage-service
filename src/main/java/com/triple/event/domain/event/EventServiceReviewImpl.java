package com.triple.event.domain.event;

import com.triple.event.web.event.request.EventRequest;
import org.springframework.stereotype.Service;

@Service
public class EventServiceReviewImpl implements EventService {

    @Override
    public boolean support(EventRequest eventRequest) {
        return eventRequest.getType() == EventType.REVIEW ? true : false;
    }

    @Override
    public void add(EventRequest eventRequest) {
        switch (eventRequest.getAction()) {
            case ADD:

                break;
            case MOD:

                break;
            case DELETE:

        }
    }

}
