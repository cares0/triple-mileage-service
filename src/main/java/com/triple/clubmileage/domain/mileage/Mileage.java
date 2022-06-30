package com.triple.clubmileage.domain.mileage;

import com.triple.clubmileage.domain.common.BaseEntity;
import com.triple.clubmileage.domain.mileagehistory.MileageHistory;
import com.triple.clubmileage.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "mileage")
    private List<MileageHistory> mileageHistories = new ArrayList<>();

    @Column(nullable = false)
    private Integer point;

    @Builder
    private Mileage(User user, Integer point) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.point = point;
    }

    public void updateMileage(Integer modifiedPoint) {
        point += modifiedPoint;
    }
}
