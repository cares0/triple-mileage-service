package com.triple.event.domain.mileage;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.triple.event.domain.mileage.QMileage.*;

public class MileageRepositoryImpl implements MileageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MileageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Mileage> findOptionalByUserId(String userId) {
        return Optional.ofNullable(queryFactory.selectFrom(mileage)
                .where(mileage.user.id.eq(userId))
                .fetchOne());
    }
}
