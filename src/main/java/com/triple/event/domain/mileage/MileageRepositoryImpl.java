package com.triple.event.domain.mileage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.event.domain.event.QEvent;
import com.triple.event.domain.mileagehistory.QMileageHistory;
import com.triple.event.domain.user.QUser;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.triple.event.domain.event.QEvent.*;
import static com.triple.event.domain.mileage.QMileage.*;
import static com.triple.event.domain.mileagehistory.QMileageHistory.*;
import static com.triple.event.domain.user.QUser.*;

public class MileageRepositoryImpl implements MileageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MileageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Mileage> findByUserId(String userId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(mileage)
                .where(mileage.user.id.eq(userId))
                .fetchOne());
    }

    @Override
    public Optional<Mileage> findByUserIdWithUser(String userId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(mileage)
                .join(mileage.user, user).fetchJoin()
                .where(mileage.user.id.eq(userId))
                .fetchOne());
    }

}
