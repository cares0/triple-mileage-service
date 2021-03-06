package com.triple.clubmileage.domain.event;

import com.triple.clubmileage.domain.event.service.EventServiceReviewImpl;
import com.triple.clubmileage.domain.mileage.Mileage;
import com.triple.clubmileage.domain.mileagehistory.MileageHistory;
import com.triple.clubmileage.domain.place.Place;
import com.triple.clubmileage.domain.review.Review;
import com.triple.clubmileage.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class EventServiceReviewImplTest {

    @Autowired
    EntityManager em;

    @Autowired
    EventServiceReviewImpl eventServiceReviewImpl;

    @Test
    @DisplayName("리뷰 저장 이벤트 - 첫 리뷰인 경우")
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

        // when

        // 글만 작성
        Event event1 = createEvent(review1, EventAction.ADD);
        eventServiceReviewImpl.add(mileage, place1, event1, "좋아요", new ArrayList<>());

        // 사진만 작성
        Event event2 = createEvent(review2, EventAction.ADD);
        List<String> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add("A");
        attachedPhotoIds.add("B");
        eventServiceReviewImpl.add(mileage, place2, event2, "", attachedPhotoIds);

        // 글 + 사진
        Event event3 = createEvent(review3, EventAction.ADD);
        eventServiceReviewImpl.add(mileage, place3, event3, "좋아요", attachedPhotoIds);

        em.flush();
        em.clear();

        Mileage findMileage = em.find(Mileage.class, mileage.getId());
        List<MileageHistory> result = findMileage.getMileageHistories();
        List<MileageHistory> mileageHistories = result.stream()
                .sorted(Comparator.comparing(MileageHistory::getCreatedDate)).collect(Collectors.toList());

        // then
        assertThat(mileageHistories.get(0).getModifiedPoint()).isEqualTo(2); // 글만 작성
        assertThat(mileageHistories.get(1).getModifiedPoint()).isEqualTo(2); // 사진만 작성
        assertThat(mileageHistories.get(2).getModifiedPoint()).isEqualTo(3); // 글 + 사진
        assertThat(findMileage.getPoint()).isEqualTo(7); // 총 마일리지
    }

    @Test
    @DisplayName("리뷰 저장 이벤트 - 첫 리뷰가 아닌 경우")
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

        Event firstEvent1 = createEvent(firstReview1, EventAction.ADD);
        Event firstEvent2 = createEvent(firstReview2, EventAction.ADD);
        Event firstEvent3 = createEvent(firstReview3, EventAction.ADD);
        eventServiceReviewImpl.add(fuMileage, place1, firstEvent1, "좋아요", new ArrayList<>());
        eventServiceReviewImpl.add(fuMileage, place1, firstEvent2, "좋아요", new ArrayList<>());
        eventServiceReviewImpl.add(fuMileage, place1, firstEvent3, "좋아요", new ArrayList<>());

        // == 두 번째 리뷰 작성 == //
        User user = createUser();
        Mileage mileage = createMileage(user);

        Review review1 = createReview(user, place1);
        Review review2 = createReview(user, place2);
        Review review3 = createReview(user, place3);

        // when

        // 글만 작성
        Event event1 = createEvent(review1, EventAction.ADD);
        eventServiceReviewImpl.add(mileage, place1, event1, "좋아요", new ArrayList<>());

        // 사진만 작성
        Event event2 = createEvent(review2, EventAction.ADD);
        List<String> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add("A");
        attachedPhotoIds.add("B");
        eventServiceReviewImpl.add(mileage, place2, event2, "", attachedPhotoIds);

        // 사진 + 글
        Event event3 = createEvent(review3, EventAction.ADD);
        eventServiceReviewImpl.add(mileage, place3, event3, "좋아요", attachedPhotoIds);

        em.flush();
        em.clear();

        Mileage findMileage = em.find(Mileage.class, mileage.getId());
        List<MileageHistory> result = findMileage.getMileageHistories();
        List<MileageHistory> mileageHistories = result.stream()
                .sorted(Comparator.comparing(MileageHistory::getCreatedDate)).collect(Collectors.toList());

        // then
        assertThat(mileageHistories.get(0).getModifiedPoint()).isEqualTo(1); // 글만 작성
        assertThat(mileageHistories.get(1).getModifiedPoint()).isEqualTo(1); // 사진만 작성
        assertThat(mileageHistories.get(2).getModifiedPoint()).isEqualTo(2); // 사진 + 글
        assertThat(findMileage.getPoint()).isEqualTo(4); // 마일리지 총점
    }

    @Test
    @DisplayName("리뷰 수정 이벤트 - 증가할 경우")
    public void 마일리지적립_MOD_사진추가() throws Exception {
        // given
        User user = createUser();
        Mileage mileage = createMileage(user);
        Place place = createPlace();
        Review review = createReview(user, place);

        // 글만 작성
        Event event = createEvent(review, EventAction.ADD);
        eventServiceReviewImpl.add(mileage, place, event, "좋아요", new ArrayList<>());

        // when

        // 사진 추가
        Event modEvent = createEvent(review, EventAction.MOD);
        List<String> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add("A");
        attachedPhotoIds.add("B");
        eventServiceReviewImpl.add(mileage, place, modEvent, "좋아요", attachedPhotoIds);

        em.flush();
        em.clear();

        Mileage findMileage = em.find(Mileage.class, mileage.getId());
        List<MileageHistory> result = findMileage.getMileageHistories();
        List<MileageHistory> mileageHistories = result.stream()
                .sorted(Comparator.comparing(MileageHistory::getCreatedDate)).collect(Collectors.toList());
        // then
        assertThat(mileageHistories.get(0).getModifiedPoint()).isEqualTo(2); // 이전 적립 이력
        assertThat(mileageHistories.get(1).getModifiedPoint()).isEqualTo(1); // 새로 생성된 적립 이력
        assertThat(findMileage.getPoint()).isEqualTo(3); // 마일리지 총점
    }

    @Test
    @DisplayName("리뷰 수정 이벤트 - 감소할 경우")
    public void 마일리지적립_MOD_사진삭제() throws Exception {
        // given
        User user = createUser();
        Mileage mileage = createMileage(user);
        Place place = createPlace();
        Review review = createReview(user, place);

        // 글 + 사진
        Event event = createEvent(review, EventAction.ADD);
        List<String> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add("A");
        attachedPhotoIds.add("B");
        eventServiceReviewImpl.add(mileage, place, event, "좋아요", attachedPhotoIds);

        // when
        // 사직 삭제
        Event modEvent = createEvent(review, EventAction.MOD);
        eventServiceReviewImpl.add(mileage, place, modEvent, "좋아요", new ArrayList<>());

        em.flush();
        em.clear();

        Mileage findMileage = em.find(Mileage.class, mileage.getId());
        List<MileageHistory> result = findMileage.getMileageHistories();
        List<MileageHistory> mileageHistories = result.stream()
                .sorted(Comparator.comparing(MileageHistory::getCreatedDate)).collect(Collectors.toList());

        // then
        assertThat(mileageHistories.get(0).getModifiedPoint()).isEqualTo(3); // 이전 적립 이력
        assertThat(mileageHistories.get(1).getModifiedPoint()).isEqualTo(-1); // 새로 생성된 적립 이력
        assertThat(findMileage.getPoint()).isEqualTo(2); // 마일리지 총점
    }

    @Test
    @DisplayName("리뷰 수정 이벤트 - 동일할 경우")
    public void 마일리지적립_MOD_동일() throws Exception {
        // given
        User user = createUser();
        Mileage mileage = createMileage(user);
        Place place = createPlace();
        Review review = createReview(user, place);

        // 글만 작성
        Event event = createEvent(review, EventAction.ADD);
        eventServiceReviewImpl.add(mileage, place, event, "좋아요", new ArrayList<>());

        // when
        // 사진만 작성
        List<String> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add("A");
        attachedPhotoIds.add("B");
        Event modEvent = createEvent(review, EventAction.MOD);
        eventServiceReviewImpl.add(mileage, place, modEvent, "", attachedPhotoIds);

        em.flush();
        em.clear();

        Mileage findMileage = em.find(Mileage.class, mileage.getId());
        List<MileageHistory> result = findMileage.getMileageHistories();
        List<MileageHistory> mileageHistories = result.stream()
                .sorted(Comparator.comparing(MileageHistory::getCreatedDate)).collect(Collectors.toList());

        // then
        assertThat(mileageHistories.get(0).getModifiedPoint()).isEqualTo(2); // 이전 적립 이력
        assertThat(mileageHistories.get(1).getModifiedPoint()).isEqualTo(0); // 새로 생성된 적립 이력
        assertThat(findMileage.getPoint()).isEqualTo(2); // 마일리지 총점
    }

    @Test
    @DisplayName("리뷰 수정 이벤트 - 여러 번 수정할 경우")
    public void 마일리지적립_MOD_여러번() throws Exception {
        // given
        User user = createUser();
        Mileage mileage = createMileage(user);
        Place place = createPlace();
        Review review = createReview(user, place);

        // 첫 리뷰 - 글만 작성
        Event event = createEvent(review, EventAction.ADD);
        eventServiceReviewImpl.add(mileage, place, event, "좋아요", new ArrayList<>());

        // when
        // 첫 번째 수정 - 사진만 작성
        List<String> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add("A");
        attachedPhotoIds.add("B");
        Event modEvent1 = createEvent(review, EventAction.MOD);
        eventServiceReviewImpl.add(mileage, place, modEvent1, "", attachedPhotoIds);

        // 두 번째 수정 - 글 + 사진
        Event modEvent2 = createEvent(review, EventAction.MOD);
        eventServiceReviewImpl.add(mileage, place, modEvent2, "좋아요", attachedPhotoIds);

        // 세 번째 수정 - 글만 작성
        Event modEvent3 = createEvent(review, EventAction.MOD);
        eventServiceReviewImpl.add(mileage, place, modEvent3, "좋아요", new ArrayList<>());
        em.flush();
        em.clear();

        Mileage findMileage = em.find(Mileage.class, mileage.getId());
        List<MileageHistory> result = findMileage.getMileageHistories();
        List<MileageHistory> mileageHistories = result.stream()
                .sorted(Comparator.comparing(MileageHistory::getCreatedDate)).collect(Collectors.toList());

        // then
        assertThat(mileageHistories.get(0).getModifiedPoint()).isEqualTo(2); // 리뷰 처음 등록했을 때 적립 이력
        assertThat(mileageHistories.get(0).getContentPoint()).isEqualTo(1);
        assertThat(mileageHistories.get(1).getModifiedPoint()).isEqualTo(0); // 첫 번째 수정 적립 이력
        assertThat(mileageHistories.get(1).getContentPoint()).isEqualTo(1);
        assertThat(mileageHistories.get(2).getModifiedPoint()).isEqualTo(1); // 두 번째 수정 적립 이력
        assertThat(mileageHistories.get(2).getContentPoint()).isEqualTo(2);
        assertThat(mileageHistories.get(3).getModifiedPoint()).isEqualTo(-1); // 세 번째 수정 적립 이력
        assertThat(mileageHistories.get(3).getContentPoint()).isEqualTo(1);
        assertThat(findMileage.getPoint()).isEqualTo(2); // 마일리지 총점
    }

    @Test
    @DisplayName("리뷰 삭제 이벤트")
    public void 마일리지적립_DELETE() throws Exception {
        // given
        User user = createUser();
        Mileage mileage = createMileage(user);
        Place place = createPlace();
        Review review = createReview(user, place);

        // 리뷰 작성
        Event event = createEvent(review, EventAction.ADD);
        eventServiceReviewImpl.add(mileage, place, event, "좋아요", new ArrayList<>());

        // when
        // 리뷰 삭제 발생
        Event modEvent = createEvent(review, EventAction.DELETE);
        eventServiceReviewImpl.add(mileage, place, modEvent, "", new ArrayList<>());

        em.flush();
        em.clear();

        Mileage findMileage = em.find(Mileage.class, mileage.getId());
        List<MileageHistory> result = findMileage.getMileageHistories();
        List<MileageHistory> mileageHistories = result.stream()
                .sorted(Comparator.comparing(MileageHistory::getCreatedDate)).collect(Collectors.toList());

        // then
        assertThat(mileageHistories.get(0).getModifiedPoint()).isEqualTo(2); // 리뷰 작성 시 적립 이력
        assertThat(mileageHistories.get(1).getModifiedPoint()).isEqualTo(-2); // 리뷰 삭제 시 적립 이력
        assertThat(findMileage.getPoint()).isEqualTo(0); // 마일리지 총점
    }

    private Event createEvent(Review review, EventAction eventAction) {
        return Event.builder().eventType(EventType.REVIEW).eventAction(eventAction).typeId(review.getId()).build();
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