package com.triple.event.domain.event;

import com.triple.event.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Event extends BaseEntity {

    @Id
    @Column(name = "event_id")
    private String id;

    private String typeId;

    @Enumerated(value = STRING)
    private EventAction eventAction;

    @Enumerated(value = STRING)
    private EventType eventType;

}
