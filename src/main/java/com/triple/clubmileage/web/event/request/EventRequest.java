package com.triple.clubmileage.web.event.request;

import com.triple.clubmileage.domain.event.Event;
import com.triple.clubmileage.domain.event.EventAction;
import com.triple.clubmileage.domain.event.EventType;
import lombok.*;

import java.util.List;

import static lombok.AccessLevel.*;

@Getter @Setter
@NoArgsConstructor(access = PRIVATE)
@Builder
@AllArgsConstructor
public class EventRequest {

    private EventType type;
    private EventAction action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;

    public Event toEvent() {
        return Event.builder()
                .typeId(reviewId)
                .eventType(type)
                .eventAction(action)
                .build();
    }

}
