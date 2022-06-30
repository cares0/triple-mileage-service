package com.triple.event.domain.mileagehistory;

import com.triple.event.domain.exception.EntityNotFoundException;
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

    public Page<MileageHistory> getPageByMileageIdWithEvent(String mileageId, Pageable pageable) {
        return mileageHistoryRepository.findPageByMileageIdWithEvent(mileageId, pageable);
    }

    public MileageHistory getOneByIdWithEvent(String mileageHistoryId) {
        return mileageHistoryRepository.findById(mileageHistoryId).orElseThrow(() ->
                new EntityNotFoundException("해당 MileageHistoryId와 일치하는 마일리지 이력을 찾을 수 없음"));
    }
}
