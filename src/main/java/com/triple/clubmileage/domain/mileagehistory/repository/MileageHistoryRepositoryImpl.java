package com.triple.clubmileage.domain.mileagehistory.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.clubmileage.domain.mileage.service.MileageCondition;
import com.triple.clubmileage.domain.mileagehistory.MileageHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

import static com.triple.clubmileage.domain.event.QEvent.*;
import static com.triple.clubmileage.domain.mileagehistory.QMileageHistory.*;

public class MileageHistoryRepositoryImpl implements MileageHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MileageHistoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MileageHistory> findAllByTypeId(String typeId) {
        return queryFactory
                .selectFrom(mileageHistory)
                .join(mileageHistory.event, event)
                .where(event.typeId.eq(typeId))
                .fetch();
    }

    @Override
    public Page<MileageHistory> findPageByMileageIdWithEvent(String mileageId, Pageable pageable, MileageCondition mileageCondition) {
        List<MileageHistory> mileageHistories = queryFactory
                .selectFrom(mileageHistory)
                .join(mileageHistory.event, event).fetchJoin()
                .where(
                        mileageHistory.mileage.id.eq(mileageId),
                        createdDateBetween(mileageCondition),
                        eventTypeEq(mileageCondition))
                .orderBy(mileageHistory.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(mileageHistory.count())
                .from(mileageHistory)
                .where(
                        mileageHistory.mileage.id.eq(mileageId),
                        createdDateBetween(mileageCondition),
                        eventTypeEq(mileageCondition));

        return PageableExecutionUtils.getPage(mileageHistories, pageable, countQuery::fetchOne);
    }

    private BooleanExpression eventTypeEq(MileageCondition mileageCondition) {
        return Objects.isNull(mileageCondition.getEventType()) ?
                null : event.eventType.eq(mileageCondition.getEventType());
    }

    private BooleanExpression createdDateBetween(MileageCondition mileageCondition) {
        return Objects.isNull(mileageCondition.getStartDate()) || Objects.isNull(mileageCondition.getEndDate()) ?
                null : mileageHistory.createdDate.between(
                        mileageCondition.getStartDate().atStartOfDay(),
                        mileageCondition.getEndDate().plusDays(1).atStartOfDay());
    }
}
