package com.triple.event.domain.mileagehistory;

import com.triple.event.domain.common.BaseEntity;
import com.triple.event.domain.event.Event;
import com.triple.event.domain.mileage.Mileage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class MileageHistory extends BaseEntity {

    @Id
    @Column(name = "mileage_history_id")
    private String id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mileage_id")
    private Mileage mileage;

    private Integer modifiedPoint;

    private ModifyingFactor modifyingFactor;

    private String content;


}
