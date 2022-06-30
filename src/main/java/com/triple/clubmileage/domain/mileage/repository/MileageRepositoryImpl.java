package com.triple.clubmileage.domain.mileage.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.clubmileage.domain.mileage.Mileage;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.triple.clubmileage.domain.mileage.QMileage.*;
import static com.triple.clubmileage.domain.user.QUser.*;

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
