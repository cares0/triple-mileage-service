package com.triple.clubmileage.web.event.adapter;

import com.triple.clubmileage.domain.event.Event;
import com.triple.clubmileage.domain.event.service.EventService;
import com.triple.clubmileage.domain.event.service.EventServiceReviewImpl;
import com.triple.clubmileage.domain.mileage.Mileage;
import com.triple.clubmileage.domain.mileage.service.MileageQueryService;
import com.triple.clubmileage.domain.place.Place;
import com.triple.clubmileage.domain.place.service.PlaceQueryService;
import com.triple.clubmileage.web.event.request.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@Transactional
@RequiredArgsConstructor
public class EventServiceReviewAdapter implements EventServiceAdapter {

    private final MileageQueryService mileageQueryService;
    private final PlaceQueryService placeQueryService;

    @Override
    public boolean support(EventService eventService) {
        return eventService instanceof EventServiceReviewImpl;
    }

    @Override
    public Map<String, String> add(EventRequest eventRequest, EventService eventService) {
        Mileage mileage = mileageQueryService.getOneByUserId(eventRequest.getUserId());
        Place place = placeQueryService.getOneById(eventRequest.getPlaceId());
        Event event = eventRequest.toEvent();

        EventServiceReviewImpl eventServiceReviewImpl = (EventServiceReviewImpl) eventService;

        String mileageHistoryId = eventServiceReviewImpl.add(mileage, place, event,
                eventRequest.getContent(), eventRequest.getAttachedPhotoIds());
        return makeIdMap(event.getId(), mileageHistoryId);
    }

    private Map<String, String> makeIdMap(String eventId, String mileageHistoryId) {
        Map<String, String> idMap = new HashMap<>();
        idMap.put("eventId", eventId);
        idMap.put("mileageHistoryId", mileageHistoryId);
        return idMap;
    }
}
