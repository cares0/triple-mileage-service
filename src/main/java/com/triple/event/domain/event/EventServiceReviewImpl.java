package com.triple.event.domain.event;

import com.triple.event.domain.exception.EntityNotFoundException;
import com.triple.event.domain.mileage.Mileage;
import com.triple.event.domain.mileagehistory.MileageHistory;
import com.triple.event.domain.mileagehistory.MileageHistoryRepository;
import com.triple.event.domain.mileagehistory.ModifyingFactor;
import com.triple.event.domain.place.Place;
import com.triple.event.domain.review.ReviewRepository;
import com.triple.event.web.event.request.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        // 이벤트 저장
        eventRepository.save(event);

        switch (event.getEventAction()) {
            case ADD:
                // 첫 리뷰인 경우 보너스 포인트
                Integer bonusPoint = getBonusPoint(place);

                // 점수 변경 요인
                ModifyingFactor modifyingFactor = getModifyingFactor(content, attachedPhotoIds);

                Integer previousPoint = 0;

                // Mileage 저장
                MileageHistory mileageHistory =
                        getMileageHistory(event, mileage, place, 0, modifyingFactor, bonusPoint);
                mileageHistory.updateMileage();
                mileageHistoryRepository.save(mileageHistory);
                break;
            case MOD:
                // 이전 이력 가져오기
                MileageHistory previousHistory = getPreviousHistory(event);

                // 수정이기에 보너스 포인트는 이전 이력과 동일
                bonusPoint = previousHistory.getBonusPoint();

                // 이전 이력에서 부여된 총 점수 계산
                previousPoint = previousHistory.getContentPoint() + previousHistory.getBonusPoint();

                // 점수 변경 요인
                modifyingFactor = getModifyingFactor(content, attachedPhotoIds);

                // 변경 요인을 가지고 내용 점수 계산, 이전 이력에서 부여된 점수가지고 변경된 점수 계산, 마일리지 업데이트 후 저장
                MileageHistory modifiedHistory =
                        getMileageHistory(event, mileage, place, previousPoint, modifyingFactor, bonusPoint);
                modifiedHistory.updateMileage();
                mileageHistoryRepository.save(modifiedHistory);
                break;
            case DELETE:

        }
    }

    private MileageHistory getPreviousHistory(Event event) {
        // reviewId를 가지고 해당 리뷰와 관련된 마일리지이력을 모두 가져옴
        List<MileageHistory> mileageHistories = mileageHistoryRepository.findAllByTypeId(event.getTypeId());
        if (CollectionUtils.isEmpty(mileageHistories)) {
            throw new EntityNotFoundException("해당 ReviewId를 가진 이벤트를 찾을 수 없음");
        }
        // 최종 변경일을 기준으로 내림차순 정렬해서 가장 최근에 변경된 이력 1개만 반환
        return mileageHistories.stream()
                .sorted(Comparator.comparing(MileageHistory::getLastModifiedDate, Comparator.reverseOrder()))
                .limit(1).collect(Collectors.toList()).get(0);
    }

    private Integer getBonusPoint(Place place) {
        // 해당 장소에 대한 리뷰가 1개라면 최초로 작성한 리뷰
        Long reviewCount = reviewRepository.getCountByPlaceId(place.getId());
        if (reviewCount <= 1) {
            return 1;
        }
        return 0;
    }

    private ModifyingFactor getModifyingFactor(String content, List<String> attachedPhotoIds) {
        // 클라이언트로부터 넘어온 내용과 사진ID 리스트만으로 판단, 따로 리뷰 테이블에서 데이터를 조회하진 않음(쿼리 한 번 덜나감)
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

    private MileageHistory getMileageHistory(Event event, Mileage mileage, Place place,
                                             Integer previousPoint, ModifyingFactor modifyingFactor,
                                             Integer bonusPoint) {
        return MileageHistory.builder()
                .event(event)
                .mileage(mileage)
                .modifyingFactor(modifyingFactor)
                // 현재 상황에서의 내용 + 보너스점수에다가, 이전 이력에서의 내용 + 보너스점수를 빼서 변경분을 구함
                .modifiedPoint((getContentPoint(modifyingFactor) + bonusPoint) - previousPoint)
                .contentPoint(getContentPoint(modifyingFactor))
                .bonusPoint(bonusPoint)
                .placeName(place.getName())
                .build();
    }

}
