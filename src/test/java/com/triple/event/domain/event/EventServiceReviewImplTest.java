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

        // 사진 입력 안한 경우
        EventRequest eventRequest1 = createEventRequest(
                user, place1, review1, EventType.REVIEW, EventAction.ADD, "좋아요!", new ArrayList<String>());

        // 글 입력 안한 경우
        List<String> attachedPhotoId = new ArrayList<>();
        attachedPhotoId.add("a");
        attachedPhotoId.add("b");
        EventRequest eventRequest2 = createEventRequest(
                user, place2, review2, EventType.REVIEW, EventAction.ADD, "", attachedPhotoId);

        // 둘 다 입력한 경우
        EventRequest eventRequest3 = createEventRequest(
                user, place3, review3, EventType.REVIEW, EventAction.ADD, "좋아요!", attachedPhotoId);

        // when
        eventServiceReviewImpl.add(eventRequest1);
        eventServiceReviewImpl.add(eventRequest2);
        eventServiceReviewImpl.add(eventRequest3);
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
        User firstUser = createUser();
        Mileage fuMileage = createMileage(firstUser);

        User user = createUser();
        Mileage mileage = createMileage(user);
        Place place1 = createPlace();
        Place place2 = createPlace();
        Place place3 = createPlace();

        Review firstReview1 = createReview(firstUser, place1);
        Review firstReview2 = createReview(firstUser, place2);
        Review firstReview3 = createReview(firstUser, place3);

        Review review1 = createReview(user, place1);
        Review review2 = createReview(user, place2);
        Review review3 = createReview(user, place3);

        EventRequest firstEventRequest1 = createEventRequest(
                firstUser, place1, firstReview1, EventType.REVIEW, EventAction.ADD, "좋아요!", new ArrayList<String>());
        EventRequest firstEventRequest2 = createEventRequest(
                firstUser, place1, firstReview1, EventType.REVIEW, EventAction.ADD, "좋아요!", new ArrayList<String>());
        EventRequest firstEventRequest3 = createEventRequest(
                firstUser, place1, firstReview1, EventType.REVIEW, EventAction.ADD, "좋아요!", new ArrayList<String>());
        eventServiceReviewImpl.add(firstEventRequest1);
        eventServiceReviewImpl.add(firstEventRequest2);
        eventServiceReviewImpl.add(firstEventRequest3);

        // 사진 입력 안한 경우
        EventRequest eventRequest1 = createEventRequest(
                user, place1, review1, EventType.REVIEW, EventAction.ADD, "좋아요!", new ArrayList<String>());

        // 글 입력 안한 경우
        List<String> attachedPhotoId = new ArrayList<>();
        attachedPhotoId.add("a");
        attachedPhotoId.add("b");
        EventRequest eventRequest2 = createEventRequest(
                user, place2, review2, EventType.REVIEW, EventAction.ADD, "", attachedPhotoId);

        // 둘 다 입력한 경우
        EventRequest eventRequest3 = createEventRequest(
                user, place3, review3, EventType.REVIEW, EventAction.ADD, "좋아요!", attachedPhotoId);

        // when
        eventServiceReviewImpl.add(eventRequest1);
        eventServiceReviewImpl.add(eventRequest2);
        eventServiceReviewImpl.add(eventRequest3);
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

    private EventRequest createEventRequest(User user, Place place, Review review, EventType eventType, EventAction eventAction, String content, List<String> attachedPhotoIds) {
        return EventRequest.builder()
                .type(eventType)
                .action(eventAction)
                .reviewId(review.getId())
                .content(content)
                .attachedPhotoIds(attachedPhotoIds)
                .userId(user.getId())
                .placeId(place.getId())
                .build();
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