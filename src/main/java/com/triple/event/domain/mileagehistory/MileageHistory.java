package com.triple.event.domain.mileagehistory;

import com.triple.event.domain.common.BaseEntity;
import com.triple.event.domain.event.Event;
import com.triple.event.domain.mileage.Mileage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.UUID;

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

    private Integer contentPoint;

    private Integer bonusPoint;

    private ModifyingFactor modifyingFactor;

    private String content;

    @Builder
    public MileageHistory(Event event, Mileage mileage, Integer contentPoint, Integer bonusPoint, ModifyingFactor modifyingFactor, String content) {
        this.id = UUID.randomUUID().toString();
        this.event = event;
        this.mileage = mileage;
        this.modifiedPoint = contentPoint + bonusPoint;
        this.contentPoint = contentPoint;
        this.bonusPoint = bonusPoint;
        this.modifyingFactor = modifyingFactor;
        this.content = content;
    }

    public void updateMileage() {
        mileage.updateMileage(modifiedPoint);
    }

}
