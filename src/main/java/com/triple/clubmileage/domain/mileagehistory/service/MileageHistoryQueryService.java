package com.triple.clubmileage.domain.mileagehistory.service;

import com.triple.clubmileage.domain.exception.EntityNotFoundException;
import com.triple.clubmileage.domain.mileage.service.MileageCondition;
import com.triple.clubmileage.domain.mileagehistory.MileageHistory;
import com.triple.clubmileage.domain.mileagehistory.repository.MileageHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MileageHistoryQueryService {

    private final MileageHistoryRepository mileageHistoryRepository;

    public Page<MileageHistory> getPageByMileageIdWithEvent(String mileageId, Pageable pageable, MileageCondition mileageCondition) {
        return mileageHistoryRepository.findPageByMileageIdWithEvent(mileageId, pageable, mileageCondition);
    }

    public MileageHistory getOneByIdWithEvent(String mileageHistoryId) {
        return mileageHistoryRepository.findById(mileageHistoryId).orElseThrow(() ->
                new EntityNotFoundException("해당 MileageHistoryId와 일치하는 마일리지 이력을 찾을 수 없음"));
    }
}
