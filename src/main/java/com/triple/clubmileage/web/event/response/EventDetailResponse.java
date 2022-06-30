package com.triple.clubmileage.web.event.response;

import com.triple.clubmileage.domain.event.Event;
import com.triple.clubmileage.domain.event.EventAction;
import com.triple.clubmileage.domain.event.EventType;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EventDetailResponse {

    private String id;
    private String typeId;
    private EventAction eventAction;
    private EventType eventType;

    public static EventDetailResponse toResponse(Event event) {
        return EventDetailResponse.builder()
                .id(event.getId())
                .typeId(event.getTypeId())
                .eventAction(event.getEventAction())
                .eventType(event.getEventType())
                .build();
    }

}
