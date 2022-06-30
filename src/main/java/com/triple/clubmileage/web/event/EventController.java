package com.triple.clubmileage.web.event;

import com.triple.clubmileage.domain.event.*;
import com.triple.clubmileage.domain.event.service.EventQueryService;
import com.triple.clubmileage.domain.event.service.EventService;
import com.triple.clubmileage.web.event.adapter.EventServiceAdapter;
import com.triple.clubmileage.web.event.provider.EventServiceAdapterProvider;
import com.triple.clubmileage.web.event.provider.EventServiceProvider;
import com.triple.clubmileage.web.event.request.EventRequest;
import com.triple.clubmileage.web.event.response.EventDetailResponse;
import com.triple.clubmileage.web.exception.ValidationFailException;
import com.triple.clubmileage.web.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventServiceProvider eventServiceProvider;
    private final EventServiceAdapterProvider eventServiceAdapterProvider;
    private final EventQueryService eventQueryService;
    private final ValidationUtils validationUtils;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> eventAdd(@Validated @RequestBody EventRequest eventRequest, BindingResult bindingResult) {
        // 요청 데이터 검증
        validationUtils.validate(bindingResult);

        // EventType에 맞는 서비스 구현체를 찾아옴
        EventService eventService = eventServiceProvider.getEventService(eventRequest);

        // 해당 서비스의 어댑터를 찾아옴
        EventServiceAdapter eventServiceAdapter = eventServiceAdapterProvider.getEventServiceAdapter(eventService);

        // 어댑터를 통해 로직 수행
        return eventServiceAdapter.adapt(eventRequest, eventService);
    }

    @GetMapping("/{eventId}")
    public EventDetailResponse eventDetail(@PathVariable String eventId) {
        Event event = eventQueryService.getOneById(eventId);
        return EventDetailResponse.toResponse(event);
    }

}
