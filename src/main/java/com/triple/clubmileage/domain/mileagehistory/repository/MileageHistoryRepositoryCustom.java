package com.triple.clubmileage.domain.mileagehistory.repository;

import com.triple.clubmileage.domain.mileage.service.MileageCondition;
import com.triple.clubmileage.domain.mileagehistory.MileageHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MileageHistoryRepositoryCustom {

    List<MileageHistory> findAllByTypeId(String typeId);

    Page<MileageHistory> findPageByMileageIdWithEvent(String mileageId, Pageable pageable, MileageCondition mileageCondition);

}
