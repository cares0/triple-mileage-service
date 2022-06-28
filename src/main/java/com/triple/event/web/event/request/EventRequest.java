package com.triple.event.web.event.request;

import com.triple.event.domain.event.Event;
import com.triple.event.domain.event.EventAction;
import com.triple.event.domain.event.EventType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.*;

@Getter @Setter
@NoArgsConstructor(access = PRIVATE)
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
                .id(UUID.randomUUID().toString())
                .typeId(reviewId)
                .eventType(type)
                .eventAction(action)
                .build();
    }

}
