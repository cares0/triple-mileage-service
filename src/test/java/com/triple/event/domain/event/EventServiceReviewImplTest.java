package com.triple.event.domain.event;

import com.triple.event.domain.mileage.Mileage;
import com.triple.event.domain.mileagehistory.MileageHistory;
import com.triple.event.domain.place.Place;
import com.triple.event.domain.review.Review;
import com.triple.event.domain.user.User;
import com.triple.event.web.event.request.EventRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class EventServiceReviewImplTest {

    @Autowired
    EntityManager em;

    @Autowired
    EventServiceReviewImpl eventServiceReviewImpl;

    @Test
    @DisplayName("마일리지 적립 - 첫 리뷰인 경우")
    public void 마일리지적립_ADD_첫리뷰() throws Exception {
        // given
        User user = createUser();
        Mileage mileage = createMileage(user);
        Place place1 = createPlace();
        Place place2 = createPlace();
        Place place3 = createPlace();

        Review review1 = createReview(user, place1);
        Review review2 = createReview(user, place2);
        Review review3 = createReview(user, place3);

        Event event1 = createEvent(review1);
        Event event2 = createEvent(review2);
        Event event3 = createEvent(review3);

        // when
        eventServiceReviewImpl.add(mileage, place1, event1, "좋아요", new ArrayList<>());
        List<String> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add("A");
        attachedPhotoIds.add("B");
        eventServiceReviewImpl.add(mileage, place2, event2, "", attachedPhotoIds);
        eventServiceReviewImpl.add(mileage, place3, event3, "좋아요", attachedPhotoIds);

        em.flush();
        em.clear();

        Mileage findMileage = em.find(Mileage.class, mileage.getId());
        List<MileageHistory> mileageHistories = findMileage.getMileageHistories();

        // then
        assertThat(mileageHistories.get(0).getModifiedPoint()).isEqualTo(2);
        assertThat(mileageHistories.get(1).getModifiedPoint()).isEqualTo(2);
        assertThat(mileageHistories.get(2).getModifiedPoint()).isEqualTo(3);
        assertThat(findMileage.getPoint()).isEqualTo(7);
    }

    @Test
    @DisplayName("마일리지 적립 - 첫 리뷰가 아닌 경우")
    public void 마일리지적립_ADD() throws Exception {
        // given
        Place place1 = createPlace();
        Place place2 = createPlace();
        Place place3 = createPlace();

        // == 첫 번째 리뷰 생성 == //
        User firstUser = createUser();
        Mileage fuMileage = createMileage(firstUser);

        Review firstReview1 = createReview(firstUser, place1);
        Review firstReview2 = createReview(firstUser, place2);
        Review firstReview3 = createReview(firstUser, place3);

        Event firstEvent1 = createEvent(firstReview1);
        Event firstEvent2 = createEvent(firstReview2);
        Event firstEvent3 = createEvent(firstReview3);
        eventServiceReviewImpl.add(fuMileage, place1, firstEvent1, "좋아요", new ArrayList<>());
        eventServiceReviewImpl.add(fuMileage, place1, firstEvent2, "좋아요", new ArrayList<>());
        eventServiceReviewImpl.add(fuMileage, place1, firstEvent3, "좋아요", new ArrayList<>());

        // == 두 번째 리뷰 작성 == //
        User user = createUser();
        Mileage mileage = createMileage(user);

        Review review1 = createReview(user, place1);
        Review review2 = createReview(user, place2);
        Review review3 = createReview(user, place3);

        Event event1 = createEvent(review1);
        Event event2 = createEvent(review2);
        Event event3 = createEvent(review3);

        // when
        eventServiceReviewImpl.add(mileage, place1, event1, "좋아요", new ArrayList<>());
        List<String> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add("A");
        attachedPhotoIds.add("B");
        eventServiceReviewImpl.add(mileage, place2, event2, "", attachedPhotoIds);
        eventServiceReviewImpl.add(mileage, place3, event3, "좋아요", attachedPhotoIds);

        em.flush();
        em.clear();

        Mileage findMileage = em.find(Mileage.class, mileage.getId());
        List<MileageHistory> mileageHistories = findMileage.getMileageHistories();

        // then
        assertThat(mileageHistories.get(0).getModifiedPoint()).isEqualTo(1);
        assertThat(mileageHistories.get(1).getModifiedPoint()).isEqualTo(1);
        assertThat(mileageHistories.get(2).getModifiedPoint()).isEqualTo(2);
        assertThat(findMileage.getPoint()).isEqualTo(4);
    }

    private Event createEvent(Review review1) {
        return Event.builder().eventType(EventType.REVIEW).eventAction(EventAction.ADD).typeId(review1.getId()).build();
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