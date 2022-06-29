package com.triple.event.domain.event;

import com.triple.event.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.EnumType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Event extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "event_id")
    private String id;

    private String typeId;

    @Enumerated(value = STRING)
    private EventAction eventAction;

    @Enumerated(value = STRING)
    private EventType eventType;

    @Builder
    private Event(String typeId, EventAction eventAction, EventType eventType) {
        this.id = UUID.randomUUID().toString();
        this.typeId = typeId;
        this.eventAction = eventAction;
        this.eventType = eventType;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
