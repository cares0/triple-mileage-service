package com.triple.event.domain.mileagehistory;

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
}
