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

import java.util.List;

import static com.triple.event.domain.event.PointCalculator.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceReviewImpl implements EventService {

    private final ReviewRepository reviewRepository;
    private final MileageHistoryRepository mileageHistoryRepository;
    private final EventRepository eventRepository;

    @Override
    public boolean support(EventRequest eventRequest) {
        return eventRequest.getType() == EventType.REVIEW ? true : false;
    }

    public void add(Mileage mileage, Place place, Event event, String content, List<String> attachedPhotoIds) {
        // ADD
        eventRepository.save(event);
        // ====

        switch (event.getEventAction()) {
            case ADD:
                // 첫 리뷰인 경우 보너스 포인트
                Integer bonusPoint = getBonusPoint(place);

                // 점수 변경 요인
                ModifyingFactor modifyingFactor = getModifyingFactor(content, attachedPhotoIds);

                // Event, Mileage 저장
                MileageHistory mileageHistory = getMileageHistory(event, mileage, place, modifyingFactor, bonusPoint);
                mileageHistory.updateMileage();
                mileageHistoryRepository.save(mileageHistory);

                break;
            case MOD:

                break;
            case DELETE:

        }
    }

    private Integer getBonusPoint(Place place) {
        Long reviewCount = reviewRepository.getCountByPlaceId(place.getId());
        Integer bonusPoint = 0;
        if (reviewCount <= 1) {
            bonusPoint = 1;
        }
        return bonusPoint;
    }

    private ModifyingFactor getModifyingFactor(String content, List<String> attachedPhotoIds) {
        ModifyingFactor modifyingFactor;
        if (!content.isBlank() && !CollectionUtils.isEmpty(attachedPhotoIds)) {
            modifyingFactor = ModifyingFactor.REVIEW_TEXT_AND_PHOTO;
        } else if (!content.isBlank()) {
            modifyingFactor = ModifyingFactor.REVIEW_TEXT;
        } else {
            modifyingFactor = ModifyingFactor.REVIEW_PHOTO;
        }
        return modifyingFactor;
    }

    private MileageHistory getMileageHistory(Event event, Mileage mileage, Place place, ModifyingFactor modifyingFactor, Integer bonusPoint) {
        return MileageHistory.builder()
                .event(event)
                .mileage(mileage)
                .modifyingFactor(modifyingFactor)
                .contentPoint(getContentPoint(modifyingFactor))
                .bonusPoint(bonusPoint)
                .placeName(place.getName())
                .build();
    }

}
