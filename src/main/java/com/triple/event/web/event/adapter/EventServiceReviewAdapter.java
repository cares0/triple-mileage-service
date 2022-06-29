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
    public void adapt(EventRequest eventRequest, EventService eventService) {
        Mileage mileage = mileageService.getOneByUserId(eventRequest.getUserId());
        Place place = placeService.getOneById(eventRequest.getPlaceId());
        Event event = eventRequest.toEvent();
        EventServiceReviewImpl eventServiceReviewImpl = (EventServiceReviewImpl) eventService;

        eventServiceReviewImpl.add(mileage, place, event, eventRequest.getContent(), eventRequest.getAttachedPhotoIds());
    }
}
