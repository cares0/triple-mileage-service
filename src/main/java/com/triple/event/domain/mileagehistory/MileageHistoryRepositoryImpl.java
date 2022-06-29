package com.triple.event.domain.mileagehistory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.event.domain.event.QEvent;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.triple.event.domain.event.QEvent.*;
import static com.triple.event.domain.mileagehistory.QMileageHistory.*;

public class MileageHistoryRepositoryImpl implements MileageHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MileageHistoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<MileageHistory> findMileageHistoryByTypeId(String typeId) {
        return Optional.ofNullable(queryFactory.selectFrom(mileageHistory)
                .join(mileageHistory.event, event).fetchJoin()
                .where(event.typeId.eq(typeId))
                .fetchOne());
    }
}
