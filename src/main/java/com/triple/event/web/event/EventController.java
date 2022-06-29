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

import java.util.Map;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventServiceProvider eventServiceProvider;
    private final EventServiceAdapterProvider eventServiceAdapterProvider;

    @PostMapping
    public Map<String, String> eventAdd(@RequestBody EventRequest eventRequest) {
        // EventType에 맞는 서비스 구현체를 찾아옴
        EventService eventService = eventServiceProvider.getEventService(eventRequest);

        // 해당 서비스의 어댑터를 찾아옴
        EventServiceAdapter eventServiceAdapter = eventServiceAdapterProvider.getEventServiceAdapter(eventService);

        // 어댑터를 통해 로직 수행
        return eventServiceAdapter.adapt(eventRequest, eventService);
    }

}
