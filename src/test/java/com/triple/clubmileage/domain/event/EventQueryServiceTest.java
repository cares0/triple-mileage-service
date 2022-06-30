package com.triple.clubmileage.domain.event;

import com.triple.clubmileage.domain.event.service.EventQueryService;
import com.triple.clubmileage.domain.mileage.Mileage;
import com.triple.clubmileage.domain.mileagehistory.MileageHistory;
import com.triple.clubmileage.domain.mileagehistory.ModifyingFactor;
import com.triple.clubmileage.domain.place.Place;
import com.triple.clubmileage.domain.review.Review;
import com.triple.clubmileage.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class EventQueryServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    EventQueryService eventQueryService;

    @Test
    @DisplayName("이벤트 - 아이디로 단건 조회")
    public void 이벤트조회_단건() throws Exception {
        // given
        User user = createUser();
        Mileage mileage = createMileage(user);
        Place place = createPlace();
        Review review = createReview(user, place);
        Event event = createEventAndHistory(review, mileage, 1);

        // when
        Event findEvent = eventQueryService.getOneById(event.getId());

        // then
        assertThat(findEvent).isEqualTo(event);
    }

    private Event createEventAndHistory(Review review, Mileage mileage, Integer bonusPoint) {
        Event event = Event.builder().eventType(EventType.REVIEW).eventAction(EventAction.ADD).typeId(review.getId()).build();
        em.persist(event);
        MileageHistory mileageHistory = MileageHistory.builder().modifiedPoint(3).bonusPoint(bonusPoint)
                .mileage(mileage).modifyingFactor(ModifyingFactor.FIRST_REVIEW)
                .event(event).contentPoint(2).content("내용").build();
        em.persist(mileageHistory);
        return event;
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