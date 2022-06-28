package com.triple.event.domain.review;

import com.triple.event.domain.common.BaseEntity;
import com.triple.event.domain.user.User;
import com.triple.event.domain.place.Place;
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
public class Review extends BaseEntity {

    @Id
    @Column(name = "review_id")
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @Builder
    private Review(Place place, User user, String content) {
        this.id = UUID.randomUUID().toString();
        this.place = place;
        this.user = user;
        this.content = content;
    }

}
