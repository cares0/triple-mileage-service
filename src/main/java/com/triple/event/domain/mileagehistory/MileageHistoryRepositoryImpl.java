package com.triple.event.domain.mileagehistory;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.triple.event.domain.event.QEvent.*;
import static com.triple.event.domain.mileagehistory.QMileageHistory.*;

public class MileageHistoryRepositoryImpl implements MileageHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MileageHistoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MileageHistory> findAllByTypeId(String typeId) {
        return queryFactory
                .selectFrom(mileageHistory)
                .join(mileageHistory.event, event).fetchJoin()
                .where(event.typeId.eq(typeId))
                .fetch();
    }

    @Override
    public Page<MileageHistory> findPageByMileageIdWithEvent(String mileageId, Pageable pageable) {
        List<MileageHistory> mileageHistories = queryFactory
                .selectFrom(mileageHistory)
                .join(mileageHistory.event, event).fetchJoin()
                .where(mileageHistory.mileage.id.eq(mileageId))
                .orderBy(mileageHistory.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(mileageHistory.count())
                .from(mileageHistory)
                .where(mileageHistory.mileage.id.eq(mileageId));

        return PageableExecutionUtils.getPage(mileageHistories, pageable, countQuery::fetchOne);
    }
}
