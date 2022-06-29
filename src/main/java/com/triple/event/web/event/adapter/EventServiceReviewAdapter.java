package com.triple.event.web.event.adapter;

import com.triple.event.domain.event.Event;
import com.triple.event.domain.event.EventService;
import com.triple.event.domain.event.EventServiceReviewImpl;
import com.triple.event.domain.mileage.Mileage;
import com.triple.event.domain.mileage.MileageService;
import com.triple.event.domain.place.Place;
import com.triple.event.domain.place.PlaceService;
import com.triple.event.web.event.request.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@Transactional
@RequiredArgsConstructor
public class EventServiceReviewAdapter implements EventServiceAdapter {

    private final MileageService mileageService;
    private final PlaceService placeService;

    @Override
    public boolean support(EventService eventService) {
        return eventService instanceof EventServiceReviewImpl;
    }

    @Override
    public Map<String, String> adapt(EventRequest eventRequest, EventService eventService) {
        Mileage mileage = mileageService.getOneByUserId(eventRequest.getUserId());
        Place place = placeService.getOneById(eventRequest.getPlaceId());
        Event event = eventRequest.toEvent();
        EventServiceReviewImpl eventServiceReviewImpl = (EventServiceReviewImpl) eventService;

        String mileageId = eventServiceReviewImpl.add(mileage, place, event,
                eventRequest.getContent(), eventRequest.getAttachedPhotoIds());
        return makeIdMap(event, mileageId);
    }

    private Map<String, String> makeIdMap(Event event, String mileageId) {
        Map<String, String> idMap = new HashMap<>();
        idMap.put("eventId", event.getId());
        idMap.put("mileageId", mileageId);
        return idMap;
    }
}
