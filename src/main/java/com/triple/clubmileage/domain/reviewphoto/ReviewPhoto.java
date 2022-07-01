package com.triple.clubmileage.domain.reviewphoto;

import com.triple.clubmileage.domain.common.BaseEntity;
import com.triple.clubmileage.domain.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ReviewPhoto extends BaseEntity {

    @Id
    @Column(name = "review_photo_id")
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

}
