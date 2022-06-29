package com.triple.event.domain.mileagehistory;

import com.triple.event.domain.event.Event;
import com.triple.event.domain.event.EventAction;
import com.triple.event.domain.event.EventType;
import com.triple.event.domain.mileage.Mileage;
import com.triple.event.domain.place.Place;
import com.triple.event.domain.review.Review;
import com.triple.event.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MileageHistoryQueryServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    MileageHistoryQueryService mileageHistoryQueryService;

    @Test
    public void 마일리지이력조회_페이징() throws Exception {
        // given
        User user = createUser();
        Mileage mileage = createMileage(user);
        Place place = createPlace();
        Review review = createReview(user, place);

        // 테스트를 위해 그냥 하나의 리뷰에 여러 마일리지가 적립되는 상황으로 가정함
        for (int i = 0; i < 15; i++) {
            createEventAndHistory(review, mileage, i);
            Thread.sleep(500);
        }
        em.flush();
        em.clear();

        // when
        PageRequest pageRequest1 = PageRequest.of(0, 3);
        Page<MileageHistory> result1 = mileageHistoryQueryService.getPageByMileageIdWithEvent(mileage.getId(), pageRequest1);
        PageRequest pageRequest2 = PageRequest.of(2, 6);
        Page<MileageHistory> result2 = mileageHistoryQueryService.getPageByMileageIdWithEvent(mileage.getId(), pageRequest2);

        // then
        assertThat(result1.getContent().size()).isEqualTo(3);
        assertThat(result1.getContent().get(0).getBonusPoint()).isEqualTo(14);
        assertThat(result1.getContent().get(1).getBonusPoint()).isEqualTo(13);
        assertThat(result1.getContent().get(2).getBonusPoint()).isEqualTo(12);

        assertThat(result2.getContent().size()).isEqualTo(3);
        assertThat(result2.getContent().get(0).getBonusPoint()).isEqualTo(2);
        assertThat(result2.getContent().get(1).getBonusPoint()).isEqualTo(1);
        assertThat(result2.getContent().get(2).getBonusPoint()).isEqualTo(0);
    }

    private MileageHistory createEventAndHistory(Review review, Mileage mileage, Integer bonusPoint) {
        Event event = Event.builder().eventType(EventType.REVIEW).eventAction(EventAction.ADD).typeId(review.getId()).build();
        em.persist(event);
        MileageHistory mileageHistory = MileageHistory.builder().modifiedPoint(3).bonusPoint(bonusPoint).mileage(mileage).modifyingFactor(ModifyingFactor.FIRST_REVIEW)
                .event(event).contentPoint(2).placeName("장소").build();
        em.persist(mileageHistory);
        return mileageHistory;
    }

    private Review createReview(User user, Place place) {
        Review review = Review.builder().content("리뷰1").place(place).user(user).build();
        em.persist(review);
        return review;
    }

    private Place createPlace() {
        Place place = Place.builder().name("장소1").build();
        em.persist(place);
        return place;
    }

    private Mileage createMileage(User user) {
        Mileage mileage = Mileage.builder().point(0).user(user).build();
        em.persist(mileage);
        return mileage;
    }

    private User createUser() {
        User user = User.builder().name("유저1").build();
        em.persist(user);
        return user;
    }

}