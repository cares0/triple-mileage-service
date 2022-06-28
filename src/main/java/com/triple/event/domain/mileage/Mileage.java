package com.triple.event.domain.mileage;

import com.triple.event.domain.common.BaseEntity;
import com.triple.event.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Mileage extends BaseEntity {

    @Id
    @Column(name = "mileage_id")
    private String id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer point;

}
