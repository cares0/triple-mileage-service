package com.triple.event.domain.mileagehistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MileageHistoryRepositoryCustom {

    List<MileageHistory> findAllByTypeId(String typeId);

    Page<MileageHistory> findPageByMileageIdWithEvent(String mileageId, Pageable pageable);

}
