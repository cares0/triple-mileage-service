package com.triple.clubmileage.web.event.request;

import com.triple.clubmileage.domain.event.Event;
import com.triple.clubmileage.domain.event.EventAction;
import com.triple.clubmileage.domain.event.EventType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static lombok.AccessLevel.*;

@Getter @Setter
@NoArgsConstructor(access = PRIVATE)
@Builder
@AllArgsConstructor
public class EventRequest {

    @NotNull
    private EventType type;

    @NotNull
    private EventAction action;

    @NotBlank
    private String reviewId;

    @NotNull
    private String content;

    @NotNull
    private List<String> attachedPhotoIds;

    @NotBlank
    private String userId;

    @NotBlank
    private String placeId;

    public Event toEvent() {
        return Event.builder()
                .typeId(reviewId)
                .eventType(type)
                .eventAction(action)
                .build();
    }

}
