package com.triple.clubmileage.domain.place;

import com.triple.clubmileage.domain.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.UUID;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Place extends BaseEntity {

    @Id
    @Column(name = "place_id")
    private String id;

    private String name;

    @Builder
    private Place(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
}
