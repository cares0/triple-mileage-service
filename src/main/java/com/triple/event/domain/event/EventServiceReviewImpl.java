package com.triple.event.domain.event;

import com.triple.event.domain.exception.EntityNotFoundException;
import com.triple.event.domain.mileage.Mileage;
import com.triple.event.domain.mileage.MileageRepository;
import com.triple.event.domain.mileagehistory.MileageHistory;
import com.triple.event.domain.mileagehistory.MileageHistoryRepository;
import com.triple.event.domain.mileagehistory.ModifyingFactor;
import com.triple.event.domain.place.Place;
import com.triple.event.domain.place.PlaceRepository;
import com.triple.event.domain.review.ReviewRepository;
import com.triple.event.web.event.request.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static com.triple.event.domain.event.PointCalculator.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceReviewImpl implements EventService {

    private final ReviewRepository reviewRepository;
    private final MileageRepository mileageRepository;
    private final PlaceRepository placeRepository;
    private final MileageHistoryRepository mileageHistoryRepository;
    private final EventRepository eventRepository;

    @Override
    public boolean support(EventRequest eventRequest) {
        return eventRequest.getType() == EventType.REVIEW ? true : false;
    }

    @Override
    public void add(EventRequest eventRequest) {
        // ADD
        Mileage mileage = mileageRepository.findOptionalByUserId(eventRequest.getUserId()).orElseThrow(() ->
                new EntityNotFoundException("해당 ID와 일치하는 회원을 찾을 수 없음"));
        Place place = placeRepository.findById(eventRequest.getPlaceId()).orElseThrow(() ->
                new EntityNotFoundException("해당 ID와 일치하는 장소를 찾을 수 없음"));
        Event event = eventRequest.toEvent();
        eventRepository.save(event);
        // ====

        switch (eventRequest.getAction()) {
            case ADD:
                // 첫 리뷰인 경우 보너스 포인트
                Integer bonusPoint = getBonusPoint(eventRequest);

                // 점수 변경 요인
                ModifyingFactor modifyingFactor = getModifyingFactor(eventRequest);

                // Event, Mileage 저장
                saveMileageHistory(event, mileage, place, modifyingFactor, bonusPoint);

                break;
            case MOD:

                break;
            case DELETE:

        }
    }

    private Integer getBonusPoint(EventRequest eventRequest) {
        Long reviewCount = reviewRepository.getCountByPlaceId(eventRequest.getPlaceId());
        Integer bonusPoint = 0;
        if (reviewCount <= 1) {
            bonusPoint = 1;
        }
        return bonusPoint;
    }

    private ModifyingFactor getModifyingFactor(EventRequest eventRequest) {
        ModifyingFactor modifyingFactor;
        if (!eventRequest.getContent().isBlank() && !CollectionUtils.isEmpty(eventRequest.getAttachedPhotoIds())) {
            modifyingFactor = ModifyingFactor.REVIEW_TEXT_AND_PHOTO;
        } else if (!eventRequest.getContent().isBlank()) {
            modifyingFactor = ModifyingFactor.REVIEW_TEXT;
        } else {
            modifyingFactor = ModifyingFactor.REVIEW_PHOTO;
        }
        return modifyingFactor;
    }

    private MileageHistory saveMileageHistory(Event event, Mileage mileage, Place place, ModifyingFactor modifyingFactor, Integer bonusPoint) {
        MileageHistory mileageHistory = MileageHistory.builder()
                .event(event)
                .mileage(mileage)
                .modifyingFactor(modifyingFactor)
                .contentPoint(getContentPoint(modifyingFactor))
                .bonusPoint(bonusPoint)
                .content(place.getName() + "에 리뷰를 작성했습니다.")
                .build();
        mileageHistory.updateMileage();
        mileageHistoryRepository.save(mileageHistory);
        return mileageHistory;
    }

}
