package com.triple.event.domain.place;

import com.triple.event.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Place extends BaseEntity {

    @Id
    @Column(name = "place_id")
    private String id;

    private String name;
}
