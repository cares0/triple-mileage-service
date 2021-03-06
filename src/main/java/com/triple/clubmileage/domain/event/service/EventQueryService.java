package com.triple.clubmileage.domain.event.service;

import com.triple.clubmileage.domain.event.Event;
import com.triple.clubmileage.domain.event.repository.EventRepository;
import com.triple.clubmileage.domain.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventQueryService {

    private final EventRepository eventRepository;

    public Event getOneById(String eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new EntityNotFoundException("해당 EventId와 일치하는 이벤트를 찾을 수 없음"));
    }

}
